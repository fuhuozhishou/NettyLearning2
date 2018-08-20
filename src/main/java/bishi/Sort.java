package bishi;

/**
 * @author zhangym
 * @version 1.0  2018/8/20
 */
public class Sort {
    /**
     * 冒泡排序，从小到大
     * @param nums 待排序数组
     * @return void
     * @author zhangym v1.0  2018/8/20
     */
    public static void bubbleSort(int[] nums){
        int temp;
        for(int i = 0; i < nums.length; i++){
            for(int j = 0; j < nums.length - i - 1; j++){
                if(nums[j] > nums[j + 1]){
                    temp = nums[j + 1];
                    nums[j + 1] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }

    /**
     * 插入排序，从小到大
     * @param nums 待排序数组
     * @return void
     * @author zhangym v1.0  2018/8/20
     */
    public static void insertSort(int[] nums){
        int index, insertValue;
        for(int i = 0; i < nums.length; i++){
            insertValue = nums[i];
            index = i - 1;
            while(index >= 0 && insertValue < nums[index]){
                nums[index + 1] = nums[index];
                index--;
            }
            nums[index + 1] = insertValue;
        }
    }


    /**
     * 输出数组中的每个元素
     * @param nums 数组
     * @return void
     * @author zhangym v1.0  2018/8/20
     */
    public static void printNumbers(int[] nums){
        for(int i : nums){
            System.out.print(i + ",");
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{3,4,2,1,5,7,3,5,1};
        System.out.print("排序前：");
        printNumbers(nums);
        System.out.println();
        insertSort(nums);
        System.out.print("排序后：");
        printNumbers(nums);
    }

}
