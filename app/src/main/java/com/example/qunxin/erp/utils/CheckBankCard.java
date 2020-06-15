package com.example.qunxin.erp.utils;

import com.example.qunxin.erp.UserBaseDatus;

/**
 * Created by qunxin on 2019/8/21.
 */

public class CheckBankCard {


    public static CheckBankCard instance=new CheckBankCard();

    public final String url="http://119.23.219.127:8094/";


    private CheckBankCard(){}

    public static CheckBankCard getInstance(){
        if(instance==null){
            instance=new CheckBankCard();
        }
        return instance;
    }
    /**

     * 校验银行卡卡号(比较算出的校验位和卡号里的校验位)

     * @param cardId

     * @return

     */

    public  boolean checkBankCard(String cardId) {

        if("".equals(cardId)){
            return false;
        }

        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));

        return cardId.charAt(cardId.length() - 1) == bit;

    }



    /**

     * 用不含校验位的银行卡卡号，采用 Luhm 校验算法获得校验位(卡号最后一位为校验位)

     * @param nonCheckCodeCardId

     * @return

     */

    public static   char getBankCardCheckCode(String nonCheckCodeCardId) {

        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0

                || !nonCheckCodeCardId.matches("\\d+")) {

            throw new IllegalArgumentException("Bank card code must be number!");

        }

        char[] chs = nonCheckCodeCardId.trim().toCharArray();

        int luhmSum = 0;

        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {

            int k = chs[i] - '0';

            if(j % 2 == 0) {

                k *= 2;

                k = k / 10 + k % 10;

            }

            luhmSum += k;

        }

        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');

    }
}
