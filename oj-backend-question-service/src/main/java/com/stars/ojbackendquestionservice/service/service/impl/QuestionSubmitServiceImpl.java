package com.stars.ojbackendquestionservice.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stars.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.stars.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;
import com.stars.ojbackendmodel.model.entity.User;
import com.stars.ojbackendmodel.model.enums.QuestionSubmitLanaugeEnum;
import com.stars.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.stars.ojbackendmodel.model.vo.QuestionSubmitVO;
import com.stars.ojbackendcommon.common.ErrorCode;
import com.stars.ojbackendcommon.constant.CommonConstant;
import com.stars.ojbackendcommon.exception.BusinessException;
import com.stars.ojbackendcommon.utils.SqlUtils;
import com.stars.ojbackendquestionservice.service.mapper.QuestionSubmitMapper;
import com.stars.ojbackendquestionservice.service.rabbitmq.MyMessageProducer;
import com.stars.ojbackendquestionservice.service.service.QuestionService;
import com.stars.ojbackendquestionservice.service.service.QuestionSubmitService;
import com.stars.ojbackendservicclient.service.JudgeFeginClient;
import com.stars.ojbackendservicclient.service.UserFeginClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 19731
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-09-02 09:54:05
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeginClient userFeginClient;

    @Resource
    @Lazy
    private JudgeFeginClient judgeFeginClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanaugeEnum lanaugeEnum = QuestionSubmitLanaugeEnum.getEnumByValue(language);
        if (lanaugeEnum == null) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }

        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 是否已题目提交
        long userId = loginUser.getId();
        // 每个用户串行题目提交
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        //  设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        // 执行判题代码
        Long questionSubmitId = questionSubmit.getId();

        //发送消息
        myMessageProducer.sendMessage("code_exchange","my_routingKey",String.valueOf(questionSubmitId));
//        CompletableFuture.runAsync(()->{
//            judgeFeginClient.doJudge(questionSubmitId);
//        });

        return questionSubmitId;

    }

    /**
     * 获取查询包装类（用户根据本哪些字段查询，根据前端传来的对象，得到mybatis框架支持的查询querymapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);

        Long userId = loginUser.getId();

        if (!questionSubmit.getUserId().equals(userId) && !userFeginClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit ->  getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




