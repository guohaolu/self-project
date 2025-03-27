package org.example.excel.entity;


import java.io.Serializable;
import java.time.Instant;

public class ProgressEntity implements Serializable {
    private String progressId;        // 进度ID
    private String taskName;          // 任务名称
    private String operator;          // 操作人
    private long operateTime;         // 操作时间
    private String appName;           // 应用名称
    private String businessType;      // 业务类型
    private int verifyProgress;       // 校验进度
    private int implementProgress;    // 实施进度
    private int total;                // 总数
    private String message;           // 消息
    private ProgressStatus status;    // 状态
    private long createdTime;         // 创建时间
    private long updatedTime;         // 更新时间

    public ProgressEntity(String progressId, String taskName, String operator,
                          String appName, String businessType, int total) {
        this.progressId = progressId;
        this.taskName = taskName;
        this.operator = operator;
        this.operateTime = Instant.now().getEpochSecond();
        this.appName = appName;
        this.businessType = businessType;
        this.verifyProgress = 0;
        this.implementProgress = 0;
        this.total = total;
        this.message = "任务初始化";
        this.status = ProgressStatus.PENDING;
        this.createdTime = Instant.now().getEpochSecond();
        this.updatedTime = this.createdTime;
    }

    public void updateProgress(int verifyProgress, int implementProgress, String message) {
        this.verifyProgress = verifyProgress;
        this.implementProgress = implementProgress;
        this.message = message;
        this.updatedTime = Instant.now().getEpochSecond();

        // 更新状态
        if (verifyProgress >= total && implementProgress >= total) {
            this.status = ProgressStatus.COMPLETED;
        } else if (verifyProgress > 0 || implementProgress > 0) {
            this.status = ProgressStatus.IN_PROGRESS;
        }
    }

    public void complete(String message) {
        this.verifyProgress = this.total;
        this.implementProgress = this.total;
        this.message = message;
        this.updatedTime = Instant.now().getEpochSecond();
        this.status = ProgressStatus.COMPLETED;
    }

    public void fail(String message) {
        this.message = message;
        this.updatedTime = Instant.now().getEpochSecond();
        this.status = ProgressStatus.FAILED;
    }

    public boolean isCompleted() {
        return this.status == ProgressStatus.COMPLETED;
    }

    public double calculateVerifyPercentage() {
        if (total <= 0) return 0;
        return Math.min(100.0, (double) verifyProgress / total * 100);
    }

    public double calculateImplementPercentage() {
        if (total <= 0) return 0;
        return Math.min(100.0, (double) implementProgress / total * 100);
    }

    // Getters and setters
    public String getProgressId() {
        return progressId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getOperator() {
        return operator;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public String getAppName() {
        return appName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public int getVerifyProgress() {
        return verifyProgress;
    }

    public int getImplementProgress() {
        return implementProgress;
    }

    public int getTotal() {
        return total;
    }

    public String getMessage() {
        return message;
    }

    public ProgressStatus getStatus() {
        return status;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public enum ProgressStatus {
        PENDING,        // 待处理
        IN_PROGRESS,    // 处理中
        COMPLETED,      // 已完成
        FAILED          // 失败
    }
}
