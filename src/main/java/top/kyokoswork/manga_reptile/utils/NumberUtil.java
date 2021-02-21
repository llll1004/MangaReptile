package top.kyokoswork.manga_reptile.utils;

public class NumberUtil {
    /**
     * 格式化编号
     *
     * @param oldNum 需要格式化的数字
     * @param length 格式化后的位数
     * @return 格式化的编号
     */
    public static String formatNumber(Integer oldNum, Integer length) {
        String num = oldNum.toString();
        // 原始位数大于目标位数,返回原始位数编号
        if (num.length() > length) return num;
        //

        return null;
    }
}
