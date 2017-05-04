import java.lang.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleCalculation {
    private int T = 0; //  变量T 用来统计正确的题数
    private int All = 0; // 变量ALL用来统计所有的题数
    private int maxcommom = 1; //  变量maxcommom 用来存储最大公约数
    private double trueProbability = 0;// 变量trueProbabity用来存储正确率
    private List<String> Formula = new ArrayList<String>();
    final int RandomRangeMax = 10;
    final int RandomRangeMin = 4;
    private DecimalFormat df = new DecimalFormat("#.00");

    // 欧几里得算法求最大公约数
    void ojld(int num1, int num2) {
        this.maxcommom = 1;
        num1 = Math.abs(num1);
        num2 = Math.abs(num2);
        if (num2 > num1) {
            int temp = num2;
            num2 = num1;
            num1 = temp;
        }
        if (num2 == 0) {
            return;
        }
        if (num1 % num2 == 0) {
            this.maxcommom = num2;
            return;
        }
        int temp = num1 % num2;
        ojld(num2, temp);
    }
    // 返回一个 随机产生的 数组形式（num[0]="1/2' num[1]="FS" 或num[0]="5' num[1]="ZS"）的 整数 或 分数
    String[] randomNum() {
        String[] number = new String[2];
        switch ((int) (Math.random() * 2)) {

            case 1: // 随机一个整数
                int num;
                num = (int) (Math.random() * RandomRangeMax) + 2;
                number[0] = String.valueOf(num);
                number[1] = "ZS";  //标记为整数
                break;
            default: //  随机一个分数
                int one1 = (int) (Math.random() * RandomRangeMin) + 1;
                int one2 = (int) (Math.random() * (RandomRangeMax - one1)) + one1 + 3;
                number[0] = String.valueOf(one1) + "/" + String.valueOf(one2);
                number[1] = "FS"; //标记为分数
                break;

        }
        return number;
    }

    //对以数组形式（num[0]="1/2' num[1]="FS"）输入的分数化简
    String[] simplifyArray(String[] num) {
        String[] str_split;
        if (num[1].equals("FS")) {
            str_split = num[0].split("/");
            ojld(Integer.parseInt(str_split[0]), Integer.parseInt(str_split[1]));
        } else {
            return num;
        }
        if (Integer.parseInt(str_split[1]) / this.maxcommom != 1) {
            num[0] = String.valueOf(Integer.parseInt(str_split[0]) / this.maxcommom + "/"
                    + Integer.parseInt(str_split[1]) / this.maxcommom);
        }
        //如果除以最大公约数后分母为1 则输出整数
        else {
            num[0] = String.valueOf(Integer.parseInt(str_split[0]) / this.maxcommom);
            num[1] = "ZS";
        }
        return num;
    }

    //算式的生成
    String formulaGeneration() {
        String number[][] = new String[20][]; //用來存儲生產的數字
        String res = "";   //用來存儲生成的算式
        int operator[] = new int[22];   //用來存儲生成的運算符
        int num;                         //用來記錄生成的位數
        int i;
        int delete = 0;                  //用來記錄合并了几位數
        num = (int) (Math.random() * 3) + 3;    //隨機生成的位數
        for (i = 0; i < num; i++) {
            number[i] = simplifyArray(randomNum());
            if (i == 0) {
                // 将第一个数左边的运算符设为空
                operator[i] = 0;
            } else {
                operator[i] = (int) (Math.random() * 4 + 1);
            }
        }
        //将最后一个数右边的运算符设为空
        operator[i] = 0;
        for (i = 1; i <= num - delete; i++) {
            if ((operator[i] == 1 && operator[i + 1] == 3) || (operator[i] == 1 && operator[i + 1] == 4)
                    || (operator[i] == 2 && operator[i + 1] == 3 || (operator[i] == 2 && operator[i + 1] == 4)
                    || operator[i - 1] == 3 && operator[i] == 1)
                    || (operator[i - 1] == 3 && operator[i] == 2) || (operator[i - 1] == 4 && operator[i] == 4)
                    || (operator[i - 1] == 4 && operator[i] == 3) || (operator[i - 1] == 4 && operator[i] == 2)
                    || (operator[i - 1] == 4 && operator[i] == 1)) {
                number[i - 1][0] = "( " + number[i - 1][0] + " " + checkOperator(operator[i]) + " " + number[i][0]
                        + " )";                     //如果滿足加括號的條件給算式加上括號並保存在前一個數中
                for (int j = i; j <= num - delete - 2; j++) {
                    number[j][0] = number[j + 1][0];       //将被合并的数字覆盖掉
                }
                for (int j = i; j < num - delete; j++) {
                    operator[j] = operator[j + 1];  //将合并的运算符覆盖掉
                }
                delete++;
            }
        }

        for (i = 0; i < num - delete; i++) {
            //得到算式
            res = res + " " + checkOperator(operator[i]) + " " + number[i][0];
        }
        return res;  //返回算式
    }

    //检查算式是否重复
    void checkFormulaRepeated(String [][]number,int operator[]) {

    }
    //根据传入的数据返回相应的运算符
    String checkOperator(int operator) {
        switch (operator) {
            case 1:
                return "+";
            case 2:
                return "-";
            case 3:
                return "×";
            case 4:
                return "÷";
            default:
                return "";
        }
    }

    //获取输入并且验证
    boolean getInputAndVerify(String strInput){
        if (strInput.trim().equals("exit")||strInput.trim().equals("oldFormula")) {
            return true;
        } else {
            strInput=strInput.replace(" ","");
            String regEx="^[+-]?[0-9]+(/[1-9][0-9]*)?$";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(strInput);
            // 字符串是否与正则表达式相匹配
            boolean rs = matcher.matches();
            return rs;
        }
    }
    //根据传入的 合法的用户数据 和 答案 验证答案 是否正确    并计算正确率和题目总数
    void verifyAnswer(String input,String answer){
        input.replace(" ","");
        this.All++;
        if(input.equals(answer)){
            this.T++;
            System.out.println("正确");
        }else{
            System.out.println("错误");
        }
        if (this.T != 0) {
            this.trueProbability = Double.parseDouble(String.valueOf(this.T)) / this.All;
        } else {
            this.trueProbability = 0;
        }
        System.out.println("答案：" + answer + "      正确率：" + df.format(trueProbability * 100) + "%\n");
    }
    //  两个数的运算
    String calculation(String leftnum, String rightnum, int operator) {
        String result = "";
        String[] num1 = new String[2];
        String[] num2 = new String[2];

        num1[0] = leftnum.replace(" ", "");
        num2[0] = rightnum.replace(" ", "");
        if (leftnum.indexOf("/") != -1) {
            num1[1] = "FS";
        } else {
            num1[1] = "ZS";
        }
        if (rightnum.indexOf("/") != -1) {
            num2[1] = "FS";
        } else {
            num2[1] = "ZS";
        }
        // 如果传入的参数是分数和分数
        if (num1[1] == "FS" && num2[1] == "FS") {
            num1 = num1[0].split("/");
            num2 = num2[0].split("/");
            int num11 = Integer.parseInt(num1[0]);
            int num12 = Integer.parseInt(num1[1]);
            int num21 = Integer.parseInt(num2[0]);
            int num22 = Integer.parseInt(num2[1]);

            //如果传入的参数是分数和整数
            switch (operator) {
                case 1:
                    result = String.valueOf((num11 * num22 + num12 * num21) + "/" + (num12 * num22));
                    break;
                case 2:
                    result = String.valueOf((num11 * num22 - num12 * num21) + "/" + (num12 * num22));
                    break;
                case 3:
                    result = String.valueOf((num11 * num21) + "/" + (num12 * num22));
                    break;
                case 4:
                    String Positive=PositiveNegativeCalculation(num1[0],num2[0]);
                    result = String.valueOf(Positive+Math.abs(num11 * num22) + "/" + Math.abs(num12 * num21));
                    break;
            }
        }
        //  如果传入的参数是分数和整数
        else if (num1[1] == "FS" && num2[1] == "ZS") {
            num1 = num1[0].split("/");
            int num11 = Integer.parseInt(num1[0]);
            int num12 = Integer.parseInt(num1[1]);
            switch (operator) {
                case 1:
                    result = String.valueOf((num11 + (num12 * Integer.parseInt(num2[0]))) + "/" + num12);
                    break;
                case 2:
                    result = String.valueOf((num11 - (num12 * Integer.parseInt(num2[0]))) + "/" + num12);
                    break;
                case 3:
                    result = String.valueOf(num11 * Integer.parseInt(num2[0]) + "/" + num12);
                    break;
                case 4:
                    String Positive=PositiveNegativeCalculation(num1[0],num2[0]);
                    result = String.valueOf(Positive+Math.abs(num11) + "/" + Math.abs(num12 * Integer.parseInt(num2[0])));
                    break;
            }
        }
        // 如果传入的参数是整数和分数
        else if (num1[1] == "ZS" && num2[1] == "FS") {
            num2 = num2[0].split("/");
            int num21 = Integer.parseInt(num2[0]);
            int num22 = Integer.parseInt(num2[1]);
            switch (operator) {
                case 1:
                    result = String.valueOf((num21 + (num22 * Integer.parseInt(num1[0]))) + "/" + num22);
                    break;
                case 2:
                    result = String.valueOf(((num22 * Integer.parseInt(num1[0])) - num21) + "/" + num22);
                    break;
                case 3:
                    result = String.valueOf(Integer.parseInt(num1[0]) * num21 + "/" + num22);
                    break;
                case 4:
                    String Positive=PositiveNegativeCalculation(num1[0],num2[0]);
                    result = String.valueOf(Positive+Math.abs(Integer.parseInt(num1[0]) * num22) + "/" + Math.abs(num21));
                    break;
            }
        }
        // 如果传入的参数是整数和整数
        else {
            switch (operator) {
                case 1:
                    result = String.valueOf(Integer.parseInt(num1[0]) + Integer.parseInt(num2[0]));
                    break;
                case 2:
                    result = String.valueOf(Integer.parseInt(num1[0]) - Integer.parseInt(num2[0]));
                    break;
                case 3:
                    result = String.valueOf(Integer.parseInt(num1[0]) * Integer.parseInt(num2[0]));
                    break;
                case 4:
                    if(num2[0].equals("0")){
                        result="无解";
                    }else if(num1[0].equals("0")){
                        result="0";
                    }else {
                        String Positive = PositiveNegativeCalculation(num1[0], num2[0]);
                        result = String.valueOf(Positive + Math.abs(Integer.parseInt(num1[0])) + "/" + Math.abs(Integer.parseInt(num2[0])));
                    }
            }
        }
        //对答案进行简化
        result =simplify(result);
        return result;
    }
    //负数除法运算符计算
    private String PositiveNegativeCalculation (String num1,String num2){
        String Positive="";
        int Positive1=0;
        int Positive2=0;
        if(Integer.valueOf(num1)>0){
            Positive1=1;
        }
        if(Integer.valueOf(num2)>0){
            Positive2=1;
        }
        switch (Positive1+Positive2){
            case 0:
            case 2:
                break;
            case 1:
                Positive="-";
                break;
        }
        return Positive;
    }
    //将整数和分数化简
    private String simplify(String result) {
        String[] str_split;
        //如果是分数
        if(result.indexOf("/")!=-1){
            str_split = result.split("/");
            //用欧几里得算法得到最大公约数
            ojld(Integer.parseInt(str_split[0]), Integer.parseInt(str_split[1]));
        }else{
            return result;
        }
        if (Integer.parseInt(str_split[1]) / this.maxcommom != 1) {
            return String.valueOf(Integer.parseInt(str_split[0]) / this.maxcommom + "/"
                    + Integer.parseInt(str_split[1]) / this.maxcommom);
        }
        //  如果除以最大公约数后分母为1 则输出整数
        else {
            return String.valueOf(Integer.parseInt(str_split[0]) / this.maxcommom);
        }

    }

    void Test() {
        String formula = formulaGeneration();
        System.out.println("第"+(this.All+1)+"题"+formula);
        Scanner sc=new Scanner(System.in);
        String res=sc.nextLine();
        while(!getInputAndVerify(res)){
            System.out.println("输入数据不合法请重新输入：");
            res=sc.nextLine();
        }
        if(res.trim().equals("exit")){
            System.out.println("\n程序已退出！期待您下次使用 ");
            System.exit(0);
        }
        verifyAnswer(res,formulaCalculation(transformationPostfixExpression(formula)));
    }
    //判断是否为数字
    boolean isNum(char c) {
        if ((c >= '0' && c <= '9') || c == '/') {
            return true;
        } else {
            return false;
        }
    }
    //将中缀表达式转换为后缀表达式
    String transformationPostfixExpression(String formula) {
        Stack<String> st = new Stack<>();
        String changeFormula = "";
        StringBuilder result = new StringBuilder();
        int topOperator = 0;
        int nowOperator = 0;
        formula = formula.replace(" ", "");
        for (int i = 0; i < formula.length(); i++) {
            result.append(" ");
            char now = formula.charAt(i);
            while (isNum(now)) {
                result.append(now);
                i++;
                if (i < formula.length()) {
                    now = formula.charAt(i);
                } else {
                    break;
                }
            }
            //如果栈不为空 读取栈首的运算符优先级
            if (!st.isEmpty()) {
                topOperator = Operator(st.lastElement());
            }
            nowOperator = Operator(String.valueOf(now));
            while (nowOperator <= topOperator) { // 如果现在的运算符优先级比栈顶小或相等
                if (topOperator != 3) {
                    if (!st.isEmpty()) { //如果栈不为
                        result.append(st.lastElement()); //  将最后一个运算符读出
                        st.pop();
                        if (!st.isEmpty()) { // 如果栈不为空
                            topOperator = Operator(String.valueOf(st.lastElement())); // 获取最新栈顶的优先数
                        } else {
                            topOperator = 0;
                        }
                    } else {
                        break;
                    }
                } else {
                    if (now==')') {
                        while (!st.lastElement().toString().equals("(")) {
                            result.append(st.lastElement());
                            st.pop();
                        }
                        st.pop();
                    }
                    break;
                }
            }
            if (!isNum(now)) {
                if (!String.valueOf(now).equals(")")) {
                    st.push(String.valueOf(now));
                }
            }
        }
        for(int i=0;i<st.size();i++){
            result.append(st.pop());
        }
        return result.toString();
    }

    //根据传入的后缀表式返回计算结果
    public String formulaCalculation(String Formula) {
        Stack formula = new Stack<>();
        char c = ' ';
        for (int i = 0; i <Formula.length(); i++) {
            c = Formula.charAt(i);
            StringBuilder number= new StringBuilder();;
            while (isNum(c)) {
                number.append(String.valueOf(c));
                i++;
                c = Formula.charAt(i);
            }
            if(!number.toString().equals("")){
                formula.push(number.toString());
            }
            if(c!=' '){
                String num1=formula.pop().toString();
                String num2=formula.pop().toString();
                String result=calculation(num2,num1, ope(c));
                formula.push(result);
                if(result.equals("无解")){
                    return "无解";
                }
            }
        }
        return formula.pop().toString();
    }
    //根据传入的运算符 返回相应的优先级
    public int Operator(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "×":
            case "÷":
                return 2;
            case "(":
                return 3;
            case ")":
                return -1;
            default:
                return 0;
        }
    }
    //根据运算符返回相应的数字
    int ope(char operatore){
        if(String.valueOf(operatore).equals("+")){
            return 1;
        }else if(String.valueOf(operatore).equals("-")){
            return 2;
        }else if(String.valueOf(operatore).equals("×")){
            return 3;
        }else{
            return 4;
        }
    }
    public static void main(String args[]) {
        SimpleCalculation s = new SimpleCalculation();
        System.out.println("欢迎使用！ 输入 exit 可退出程序！");
        while (true) {
            s.Test();
        }
    }
}