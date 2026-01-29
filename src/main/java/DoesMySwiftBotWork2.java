import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import swiftbot.SwiftBotAPI; // 导入 SwiftBot API
import swiftbot.Button;      // 导入 Button 类

public class DoesMySwiftBotWork2 {

    static SwiftBotAPI swiftBot; // 声明 swiftBot 对象

    public static void main(String[] args) throws InterruptedException {
        // 初始化 swiftBot
        try {
            swiftBot = new SwiftBotAPI();
        } catch (Exception e) {
            System.out.println("Failed to initialize SwiftBot.");
            e.printStackTrace();
            System.exit(1);
        }

        // 动态数组存储 Simon 游戏的随机颜色序列
        List<int[]> randomColourSequence = new ArrayList<>();

        int remainingChances = 3; // 玩家初始有三次重新开始的机会
        int score = 0; // 当前分数
        int highestScore = 0; // 最高分数

        while (true) {
            System.out.println("Starting Simon Game...");
            randomColourSequence.clear(); // 清空颜色序列
            score = 0; // 重置当前分数

            while (true) {
                // 调用生成随机颜色的方法
                int[] generatedColour = RandomUnderlight();

                // 将返回的颜色值存储到序列中
                randomColourSequence.add(generatedColour);

                // 显示当前轮的完整序列
                System.out.println("\nRound " + (score + 1) + ": Simon says:");
                for (int[] colour : randomColourSequence) {
                    DisplayColour(colour); // 显示颜色
                }

                // 玩家输入并验证
                System.out.println("Your turn! Repeat the sequence:");
                if (generateUserInput(randomColourSequence) == null) {
                    // 如果玩家输入错误，游戏结束
                    System.out.println("Game Over! You made a mistake.");
                    System.out.println("Your final score: " + score);

                    // 更新最高分
                    if (score > highestScore) {
                        highestScore = score;
                        System.out.println("Congratulations! New highest score: " + highestScore);
                    } else {
                        System.out.println("Highest score so far: " + highestScore);
                    }

                    if (score >= 5) {
                        // 调用庆祝方法
                        System.out.println("Celebrating your effort!");
                        celebrationDive(score);
                    }

                    // 检查是否还有重新开始的机会
                    if (remainingChances > 0) {
                        if (askToRestart(remainingChances)) {
                            remainingChances--; // 减少一次机会
                            break; // 跳出当前游戏循环，重新开始
                        } else {
                            System.out.println("Thank you for playing! Goodbye.");
                            return; // 退出游戏
                        }
                    } else {
                        System.out.println("No more chances left. Thank you for playing!");
                        return; // 退出游戏
                    }
                }

                // 输入正确，加分并进入下一轮
                score++;
                System.out.println("Correct! Your current score is: " + score);
                System.out.println("Get ready for the next round.");
                System.out.println("Highest score so far: " + highestScore);
            }
        }
    }

    public static int[] RandomUnderlight() {
        int[][] colours = {
            {255, 0, 0}, // 红色 Red
            {0, 255, 0}, // 绿色 Green
            {0, 0, 255}, // 蓝色 Blue
            {255, 255, 0} // 黄色 Yellow
        };

        try {
            Random random = new Random();
            int randomIndex = random.nextInt(colours.length); // 随机选择一个颜色 Randomly select a color
            int[] rgb = colours[randomIndex]; // 获取对应的颜色值

            // 打印生成的颜色（可选）
            System.out.println("Generated RGB: [" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "]");

            return rgb; // 返回生成的颜色 Return the generated color
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to generate a random color");
            System.exit(5); // 强制退出 Exit the program
            return null; // 这里实际上不会执行，因为 System.exit 会终止程序
        }
    }

    // 显示颜色
    public static void DisplayColour(int[] colour) throws InterruptedException {
        swiftBot.fillUnderlights(colour); // 显示颜色
        System.out.println("Displaying: [" + colour[0] + ", " + colour[1] + ", " + colour[2] + "]");
        Thread.sleep(500); // 保持颜色显示
        swiftBot.disableUnderlights(); // 关闭灯光
        Thread.sleep(200); // 添加短间隔
    }

    // 生成用户输入并验证
    public static List<int[]> generateUserInput(List<int[]> targetSequence) throws InterruptedException {
        List<int[]> userInputSequence = new ArrayList<>(); // 动态数组存储用户输入的颜色
        int inputCount = targetSequence.size(); // 玩家需要输入的次数等于目标序列长度
        int[][] colours = {
            {255, 0, 0}, // Red for A
            {0, 0, 255}, // Blue for B
            {0, 255, 0}, // Green for X
            {255, 255, 0} // Yellow for Y
        };

        System.out.println("Please press buttons on the SwiftBot (A, B, X, Y):");

        try {
            // 用于捕获用户输入的变量
            final char[] lastPressedButton = { '\0' }; // 使用数组以便匿名函数修改其值

            // 启用按钮并绑定回调函数
            swiftBot.enableButton(Button.A, () -> {
                System.out.println("Button A Pressed.");
                lastPressedButton[0] = 'A';
            });

            swiftBot.enableButton(Button.B, () -> {
                System.out.println("Button B Pressed.");
                lastPressedButton[0] = 'B';
            });

            swiftBot.enableButton(Button.X, () -> {
                System.out.println("Button X Pressed.");
                lastPressedButton[0] = 'X';
            });

            swiftBot.enableButton(Button.Y, () -> {
                System.out.println("Button Y Pressed.");
                lastPressedButton[0] = 'Y';
            });

            // 按输入次数轮询用户操作
            for (int i = 0; i < inputCount; i++) {
                System.out.println("Waiting for input " + (i + 1) + " of " + inputCount + "...");

                // 等待用户按下有效按钮
                while (lastPressedButton[0] == '\0') {
                    Thread.sleep(50); // 防止高频轮询占用资源
                }

                // 将按钮映射到对应颜色
                int[] selectedColor;
                switch (lastPressedButton[0]) {
                    case 'A':
                        selectedColor = colours[0];
                        break;
                    case 'B':
                        selectedColor = colours[1];
                        break;
                    case 'X':
                        selectedColor = colours[2];
                        break;
                    case 'Y':
                        selectedColor = colours[3];
                        break;
                    default:
                        System.out.println("Invalid button detected. Please press A, B, X, or Y.");
                        i--; // 重试当前输入
                        lastPressedButton[0] = '\0'; // 重置按钮状态
                        continue;
                }

                // 验证用户输入是否正确
                if (!isCorrect(selectedColor, targetSequence.get(i))) {
                    System.out.println("Wrong input! Game Over.");
                    swiftBot.disableAllButtons(); // 禁用所有按钮
                    return null; // 输入错误时，返回 null
                }

                // 添加到用户输入序列中
                userInputSequence.add(selectedColor);
                lastPressedButton[0] = '\0'; // 重置按钮状态
            }

            System.out.println("User input sequence recording completed.");
            swiftBot.disableAllButtons(); // 禁用所有按钮
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in capturing button press. Try again.");
            swiftBot.disableAllButtons(); // 禁用所有按钮
            return null;
        }

        return userInputSequence; // 返回用户输入的完整序列
    }
    
   

    public static boolean isCorrect(int[] inputColor, int[] targetColor) {
        if (inputColor.length != targetColor.length) return false;
        for (int i = 0; i < inputColor.length; i++) {
            if (inputColor[i] != targetColor[i]) return false;
        }
        return true;
    }

    public static boolean askToRestart(int remainingChances) {
        System.out.println("You have " + remainingChances + " chance(s) remaining.");
        System.out.println("Do you want to restart the game? (yes/no):");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();

        return response.equals("yes");
    }

 // Test Dive
 		public static void celebrationDive(int userScore) throws InterruptedException {
 			int speed;
 			
 			// Speed based on user score
 			if (userScore >=10) {
 				speed = 100;
 			}
 			else if (userScore >= 5) {
 				speed = userScore * 10;
 			}
 			else {
 				speed = 40;
 				
 			}
 			// Random RGBY blink
 			celebrationBlink();
 			
 			// Display Celebration speed
 			System.out.println("Celebration dive V at speed: " + speed);
 			
 			// \first leg of the V 
 			swiftBot.move(100,-100, speed);
 			
 			swiftBot.move(speed, speed, Time(150, speed));
 			
 			swiftBot.move(-speed, -speed, Time(150, speed));
 			Thread.sleep(500);
 			swiftBot.move(-100, 100, speed);
 			Thread.sleep(500);
 			swiftBot.move(-100, 100, speed);
 			swiftBot.move(-100, 100, speed);
 			Thread.sleep(500);

 			// /second leg of the v 
 			swiftBot.move(speed, speed, Time(150, speed));
 			swiftBot.move(-speed, -speed, Time(150, speed));
 			Thread.sleep(500);
 			swiftBot.move(100, -100, speed);
 			Thread.sleep(500);
 			
 			celebrationBlink();
 			System.out.println("Celebration dive complete :)");
 		}
 		
 		public static void celebrationBlink() throws InterruptedException {
 			int[][] colours = {
 					{ 255, 0, 0 }, // Red
 					{ 0, 255, 0 }, // Green
 					{ 0, 0, 255 }, // Blue
 					{ 255, 255, 0 } // Yellow
 			};

 			Random random = new Random();
 			for (int i = 0; i < colours.length; i++) {
 				int randomIndex = random.nextInt(colours.length);
 				int[] rgb = colours[randomIndex];
 				swiftBot.fillUnderlights(rgb);
 				Thread.sleep(300);
 				
 				
 			}
 			swiftBot.disableUnderlights();
 		}
 		
 		public static int Time(int distanceCm, int speed) {
 			// T=D/S with speed conversion
 			return (distanceCm * 1000) / speed;
 			
 			
 		}
}
