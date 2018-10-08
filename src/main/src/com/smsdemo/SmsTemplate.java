package com.smsdemo;

public enum SmsTemplate {
    /**
     * 短信验证码
     */
    VerifyCode(2),//验证码模版：验证码，超时（分）
    Session2FA(2),//会话双因子认证：超时（分）、验证码
    SystemUpgrade(2),//升级通知：开始时间，结束时间
    MonitorAlert(3),//监控预警
    CostAlert(2),//后付费资源消费预警
    AlertRecover(4)//告警恢复
    ;
    private int paramLength;//模版需要的参数长度

    SmsTemplate(int paramLength) {
        this.paramLength = paramLength;
    }

    public int getParamLength() {
        return paramLength;
    }
}
