import java.lang.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by wcn on 2017/3/21.
 */
public class SimpleCalculation {
    private int T=0;                    // 变量T 用来统计正确的题数
    private int All=0;                  //变量ALL用来统计所有的题数
    private int maxcommom=1;           //变量maxcommom 用来存储最大公约数
    private double trueProbability=0;//变量trueProbabity用来存储正确率
    private List<String> Formula=new ArrayList<String>();
    public SimpleCalculation(){

    }

    void ojld(int num1,int num2){                                           //欧几里得算法求最大公约数
        num1=Math.abs(num1);
        num2=Math.abs(num2);
        if(num2>num1){
            int temp=num2;
            num2=num1;
            num1=temp;
        }
        if(num2==0){
            return;
        }
        if(num1%num2==0){
            this.maxcommom=num2;
            return;
        }
        int temp=num1%num2;
        ojld(num2,temp);
    }
    String [] randomnum(){                                              //随机产生整数或分数
        String [] number=new String[2];
        switch ((int)(Math.random()*2)) {

            case 1:                                                     //随机一个整数
                int num;
                num = (int) (Math.random() * 99) + 1;
                number[0] = String.valueOf(num);
                number[1] = "ZS";
                break;
            default:                                                    //随机一个分数
                int one1 = (int) (Math.random() * 60) + 1;
                int one2 = (int) (Math.random() * (99 - one1)) + one1 + 1;
                number[0] = String.valueOf(one1) + "/" + String.valueOf(one2);
                number[1] = "FS";
                break;
        }
        return number;
    }
    void Test(){

        String num1[]=randomnum();
        String num2[]=randomnum();
        int switchnum=(int)(Math.random()*4)+1;
        switch (switchnum){
            case 1:
                System.out.println(num1[0]+" + "+num2[0]+"=");
                break;
            case 2:
                System.out.println(num1[0]+" - "+num2[0]+"=");
                break;
            case 3:
                System.out.println(num1[0]+" * "+num2[0]+"=");
                break;
            case 4:
                System.out.println(num1[0]+" ÷ "+num2[0]+"=");
                break;
        }
        CheckFormulaRepeated(num1,num2,switchnum);
    }
    void CheckFormulaRepeated(String []num1,String [] num2,int operator){
        boolean repeated=false;
        String ope=checkoperator(operator);
        while(true){
            if(ope.equals("+")||ope.equals("*")){
                    for (int i = 0; i < Formula.size(); i++) {
                        if (Formula.get(i).equals(num1[0] + ope + num2[0]) || Formula.get(i).equals(num2[0] + ope + num1[0])) {
                            repeated = true;
                        }
                    }
            }else{
                    for(int i=0;i<Formula.size();i++){
                        if(Formula.get(i).equals(num1[0]+ope+num2[0])){
                            repeated=true;
                        }
                    }
            }
            if(!repeated){
                ope=checkoperator(operator);
                if(ope.equals("")){
                    System.out.print("运算符随机错误！！！运算符未找到！！！");
                    System.exit(0);
                }
                Formula.add(num1[0]+ope+num2[0]);
                calculation(num1,num2,operator);
                break;
            }else{
                num1=randomnum();
                num2=randomnum();
                operator=(int)(Math.random()*4)+1;
            }
        }
    }
    String checkoperator(int operator){
        switch (operator){
            case 1:
                return "+";
            case 2:
                return "-";
            case 3:
                return "*";
            case 4:
                return "/";
            default:
                return "";
         }
    }
    boolean checked(String string){                             //检测字符串是否含有除 数字0123456789 空格 和  。（点） 和 / 之外的字符
        int length=0;
        for(int i=0;i<string.length();i++){
            char charat=string.charAt(i);
			if(i==0&&charat=='-'){
				length++;
			}
            if(charat=='0'||charat=='1'||charat=='2'||charat=='3'||charat=='4'||charat=='5'||charat=='6'||charat=='7'||charat=='8'||charat=='9'||charat=='/'||charat==' '||charat=='.'){
                length++;
            }
        }
        if(length==string.length()){
            return true;
        }else{
            return false;
        }
    }
    void calculation(String []num1,String [] num2,int yunsf) {
        Scanner strin=new Scanner(System.in);
        String result="";
        DecimalFormat df=new DecimalFormat("#.00");
        this.maxcommom=1;

        //如果传入的参数是分数和分数
        if (num1[1] == "FS" && num2[1] == "FS") {
            num1=num1[0].split("/");
            num2=num2[0].split("/");
            int num11=Integer.parseInt(num1[0]);
            int num12=Integer.parseInt(num1[1]);
            int num21=Integer.parseInt(num2[0]);
            int num22=Integer.parseInt(num2[1]);

            //根据传入的运算符 获得未化简得计算结果
            switch (yunsf) {
                case 1:
                    result=String.valueOf((num11*num22+num12*num21)+"/"+(num12*num22));//需要化简
                    break;
                case 2:
                    result=String.valueOf((num11*num22-num12*num21)+"/"+(num12*num22));//需要化简
                    break;
                case 3:
                    result=String.valueOf((num11*num21)+"/"+(num12*num22));//需要化简
                    break;
                case 4:
                    result=String.valueOf((num11*num22)+"/"+(num12*num21));//需要化简
                    break;
            }
            String [] res=result.split("/");

            //运用欧几里得算法求最大公约数
            ojld(Integer.parseInt(res[0]),Integer.parseInt(res[1]));

            //如果除以最大公约数后分母不唯1 则输出分数
            if(Integer.parseInt(res[1])/this.maxcommom!=1){
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom+"/"+Integer.parseInt(res[1])/this.maxcommom);
            }
            //如果除以最大公约数后分母为1 则输出整数
            else{
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom);
            }
        }
        //如果传入的参数是分数和整数
        else if (num1[1] == "FS" && num2[1]=="ZS") {
            num1=num1[0].split("/");
            int num11=Integer.parseInt(num1[0]);
            int num12=Integer.parseInt(num1[1]);
            switch (yunsf) {
                case 1:
                    result=String.valueOf((num11+(num12*Integer.parseInt(num2[0])))+"/"+num12);//需要化简
                    break;
                case 2:
                    result=String.valueOf((num11-(num12*Integer.parseInt(num2[0])))+"/"+num12);//需要化简
                    break;
                case 3:
                    result=String.valueOf(num11*Integer.parseInt(num2[0])+"/"+num12);//需要化简
                    break;
                case 4:
                    result=String.valueOf(num11+"/"+num12*Integer.parseInt(num2[0]));//需要化简
                    break;
            }
            String [] res=result.split("/");
            ojld(Integer.parseInt(res[0]),Integer.parseInt(res[1]));

            if(Integer.parseInt(res[1])/this.maxcommom!=1){
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom+"/"+Integer.parseInt(res[1])/this.maxcommom);
            }else{
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom);
            }
        }
        //如果传入的参数是整数和分数
        else if (num1[1] == "ZS" && num2[1] == "FS") {
            num2=num2[0].split("/");
            int num21=Integer.parseInt(num2[0]);
            int num22=Integer.parseInt(num2[1]);
            switch (yunsf) {
                case 1:
                    result=String.valueOf((num21+(num22*Integer.parseInt(num1[0])))+"/"+num22);//需要化简
                    break;
                case 2:
                    result=String.valueOf(((num22*Integer.parseInt(num1[0]))-num21)+"/"+num22);//需要化简
                    break;
                case 3:
                    result=String.valueOf(Integer.parseInt(num1[0])*num21+"/"+num22);//需要化简
                    break;
                case 4:
                    result=String.valueOf(Integer.parseInt(num1[0])*num22+"/"+num21);//需要化简
                    break;
            }
            String [] res=result.split("/");
            ojld(Integer.parseInt(res[0]),Integer.parseInt(res[1]));
            if(Integer.parseInt(res[1])/this.maxcommom!=1){
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom+"/"+Integer.parseInt(res[1])/this.maxcommom);
            }else{
                result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom);
            }
        }
        //如果传入的参数是整数和整数
        else {
            switch (yunsf) {
                case 1:
                    result=String.valueOf(Integer.parseInt(num1[0]) + Integer.parseInt(num2[0]));
                    break;
                case 2:
                    result=String.valueOf(Integer.parseInt(num1[0]) - Integer.parseInt(num2[0]));
                    break;
                case 3:
                    result=String.valueOf(Integer.parseInt(num1[0]) * Integer.parseInt(num2[0]));
                    break;
                case 4:
                    result=String.valueOf(Integer.parseInt(num1[0]) +"/"+ Integer.parseInt(num2[0]));
                    String res[]=result.split("/");
                    ojld(Integer.parseInt(res[0]),Integer.parseInt(res[1]));
                    if(Integer.parseInt(res[1])/this.maxcommom!=1){
                        result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom+"/"+Integer.parseInt(res[1])/this.maxcommom);
                    }else{
                        result=String.valueOf(Integer.parseInt(res[0])/this.maxcommom);
                    }
                    break;
            }
        }
        System.out.println("请输入本题答案或命令（exit 退出程序   oldFormula 查看已生成的算式）（最简）：");

        //得到用户输入的数据
        String strinput = strin.nextLine();
        while(true) {
            //判断是否输入的是否为退出指令
            if (strinput.equals("exit")) {
                System.out.println("程序已退出！欢迎再次使用！");
                System.exit(0);
            }else if(strinput.equals("oldFormula")){
                System.out.println("已生成的算式：");
                for(int i=0;i<Formula.size();i++){
                    System.out.println("第"+(i+1)+"题："+Formula.get(i));
                }
                System.out.println("请输入本题答案或命令（exit 退出程序   oldFormula 查看已生成的算式）（最简）：");
                strinput = strin.nextLine();
            }else{
                boolean check=checked(strinput);
                //检查字符串是否含其他字符
                if(check){
                    //检查字符串是否为分数
                    if (strinput.indexOf("/") != -1) {
                        String strinputarr[]= strinput.split("/");
                        //检查分数是否合法（粗略检测格式是否合法）
                        if(strinputarr.length>1&&!strinputarr[0].equals("")){
                            //检查分数是否合法（仔细检测分母是否为0）
                            if (strinputarr[1].equals("0")) {
                                System.out.print("输入数据不合法请重新输入：");
                                strinput = strin.nextLine();
                            }
                            //输入的分数合法对答案进行比较
                            else {
                                if (strinput.equals(result)) {
                                    System.out.println("正确！");
                                    this.T += 1;
                                } else {
                                    System.out.println("错误！");
                                }
                                this.All += 1;
                                break;
                            }
                        }
                        //输入的分数不合法需要重新输入
                        else{
                            System.out.print("输入数据不合法请重新输入：");
                            strinput = strin.nextLine();
                        }
                    }else {
                        if (strinput.equals(result)) {
                            System.out.println("正确！");
                            this.T += 1;
                        } else {
                            System.out.println("错误！");
                        }
                        this.All += 1;
                        break;
                    }
                }else{
                    System.out.print("输入数据不合法请重新输入：");
                    strinput = strin.nextLine();
                }
            }
        }
        if(this.T!=0) {
            this.trueProbability = Double.parseDouble(String.valueOf(this.T))/ this.All;
        }else{
            this.trueProbability=0;
        }
        System.out.println("答案："+result+"      正确率："+df.format(trueProbability*100)+"%");
    }

    public static void main(String args[]){
        SimpleCalculation s=new SimpleCalculation();
        System.out.println("欢迎使用！");
        while (true) {
            s.Test();
        }
    }
}