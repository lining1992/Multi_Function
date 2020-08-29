package com.example.commonlibrary.android.viewpager.table;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/23 21:32
 */
public class TablePagerUtil {
    private TablePagerUtil() {
        throw new UnsupportedOperationException();
    }

    public static int getPosition(int pageIndex, int row, int column, int rowSize, int columnSize) {
        return pageIndex * rowSize * columnSize + row * columnSize + column;
    }

    public static int[] getIndexArray(int position, int rowSize, int columnSize) {
        int[] indexArray = new int[3];
        int pageSize = rowSize * columnSize;

        indexArray[0] = position / pageSize;
        position %= pageSize;
        indexArray[1] = position / columnSize;
        indexArray[2] = position % rowSize;

        return indexArray;
    }
}
