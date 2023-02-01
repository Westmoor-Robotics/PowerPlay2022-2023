package org.firstinspires.ftc.teamcode.AutonComp2022;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.robot.Robot;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.AutonComp2022.Robot2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.List;

public class Movement2022 {
    Robot2022 robot = null;
    LinearOpMode opMode = null;

    //double power = 1;
    //double cmCountY = 15.5;
    //double cmCountX = 50;

    public Movement2022(Robot2022 arobot, LinearOpMode aOpMode){
        robot = arobot;
        opMode = aOpMode;
    }

    public void move(double x, double y, double turn, double power){

        robot.frontleft.setPower(power*(y + x - turn));
        robot.backleft.setPower(power*(y - x - turn));
        robot.frontright.setPower(power*(y - x + turn));
        robot.backright.setPower(power*(y + x + turn));
    }

    public void forward(double seconds, double power) {
        move(0, -1, 0, power);
        opMode.sleep((long) (seconds*1000));
    }

    public void backward(double seconds, double power) {
        move(0, 1, 0, power);
        opMode.sleep((long) (seconds*1000));
    }

    public void strafeLeft(double seconds, double power) {
        move(-1, 0, 0, power);
        opMode.sleep((long) (seconds*1000));
    }

    public void strafeRight(double seconds, double power) {
        move(1, 0, 0, power);
        opMode.sleep((long) (seconds*1000));
    }

    public void stop(double seconds) {
        move(0, 0, 0, 0);
        opMode.sleep((long) (seconds*1000));
    }
    //public int scan() {return 1;}

    /*
    String number = "1";
    int soemthing = number;

    */
    public int scan(int seconds, Telemetry telemetry) {

        robot.color_sensor.enableLed(true);

        String color = "No Color Found";
        int cutoff = 40;
        int red = robot.color_sensor.red();
        int blue = robot.color_sensor.blue();
        int green = robot.color_sensor.green();
        double time = robot.runtime.time();
        int status = 0;

        while(color == "No Color Found" && (robot.runtime.time() - time) < seconds && opMode.opModeIsActive()) {
            time = robot.runtime.time();

            if (Math.abs(red - blue) < cutoff && Math.abs(red - green) < cutoff && Math.abs(green - blue) < cutoff) {
                color = "No Color Found";
            } else if(red > blue && red > green) {
                color = "Red";
                status = 1;
            } else if (green > blue && green > red) {
                color = "Green";
                status = 2;
            } else if (blue > red && blue > green){
                color = "Blue";
                status = 3;
            }

            cutoff -= .5; // if something doesn't work, decrease your expectations until it does
            red = robot.color_sensor.red();
            blue = robot.color_sensor.blue();
            green = robot.color_sensor.green();
            strafeLeft(.1, .4);
            telemetry.addData("Cutoff", cutoff);
            telemetry.addData("Red", red);
            telemetry.addData("Green", green);
            telemetry.addData("Blue", blue);
            telemetry.update();

        }
        return status;
    }

    /*public void moveY(double cm, double speed, double timeoutS) {
        if(opMode.opModeIsActive()) {

            int target = robot.frontright.getCurrentPosition() + (int) (cm);

            robot.frontright.setTargetPosition(target);
            robot.frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.runtime.reset();

            while(opMode.opModeIsActive() && robot.frontright.isBusy() && (robot.runtime.seconds() < timeoutS)) {
            opMode.telemetry.addData("Target", "Running to %7d : %7d", newLeftTarget, newRightTarget);
            opMode.telemetry.addData("Current", "Running to %7d : %7d", robot.frontleft.getCurrentPosition(), robot.backright.getCurrentPosition());
            opMode.telemetry.update();

                if(cm < 0){
                    move(0, -1, 0, speed);
                } else if(cm >= 0){
                    move(0, 1, 0, speed);
                }
            }
            move(0, 0, 0, 0); // stop
            //opMode.sleep(250); //250 = 0.25 seconds
        }
    }

    public void moveX(double cm, double speed, double timeoutS) {
        if(opMode.opModeIsActive()) {

            int target = robot.frontright.getCurrentPosition() + (int) (cm*cmCountX);

            robot.frontright.setTargetPosition(target);
            robot.frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.runtime.reset();

            while(opMode.opModeIsActive() && robot.frontright.isBusy() && (robot.runtime.seconds() < timeoutS)) {
            opMode.telemetry.addData("Target", "Running to %7d : %7d", newLeftTarget, newRightTarget);
            opMode.telemetry.addData("Current", "Running to %7d : %7d", robot.frontleft.getCurrentPosition(), robot.backright.getCurrentPosition());
            opMode.telemetry.update();

                if(cm < 0){
                    move(-1, 0, 0, speed);
                } else if(cm >= 0){
                    move(1, 0, 0, speed);
                }
            }
            move(0, 0, 0, 0); // stop
            //opMode.sleep(250); //250 = 0.25 seconds
        }
    }*/

    public void turn(int bearing, double speed) {
        double idkhowtonamevariablesrn = 0;
        robot.initIMU(opMode.hardwareMap);
        Orientation angles = robot.imu.getAngularOrientation();
        float heading = -angles.firstAngle;

        if(bearing < heading) {
            idkhowtonamevariablesrn = -1;
        } else if (bearing > heading) {
            idkhowtonamevariablesrn = 1;
        }

        //bearing += Math.abs(Math.round(heading))-10;


        while(Math.abs(bearing) > Math.abs(heading)) {
            angles = robot.imu.getAngularOrientation();
            heading = -angles.firstAngle;

            opMode.telemetry.addData("bearing", bearing);
            opMode.telemetry.addData("heading", heading);
            opMode.telemetry.addData("current angle bot is at", angles.firstAngle);
            opMode.telemetry.update();

            move(0, 0, idkhowtonamevariablesrn, speed);

            if(opMode.isStopRequested()) {break;}
        }
    }

    public void openServo() {
        robot.rightGrab.setPosition(.8);
        robot.leftGrab.setPosition(.2); // open
    }
    public void closeServo() {
        robot.leftGrab.setPosition(.27); // close
        robot.rightGrab.setPosition(.73);
    }

    public void liftUp(double seconds) {
        robot.leftlift.setPower(.75);
        robot.rightlift.setPower(.75);
        opMode.sleep((long) (seconds*1000));
        robot.leftlift.setPower(0);
        robot.rightlift.setPower(0);

    }

    public void liftDown(double seconds) {
        robot.leftlift.setPower(-.75);
        robot.rightlift.setPower(-.75);
        opMode.sleep((long) (seconds*1000));
        robot.leftlift.setPower(0);
        robot.rightlift.setPower(0);

    }
    public void liftStop() {
        robot.leftlift.setPower(0);
        robot.rightlift.setPower(0);
    }

    /*public void accelerationfunctionwhatever(double power) {
        double currentPower = 0;

        while(p)
        if(opMode.gamepad1.right_stick_y > 0) {
            return power + 0.01;
        } else if (opMode.gamepad1)
    }*/

    /*public void turnLeft(int bearing){
        robot.initIMU(opMode.hardwareMap);
        Orientation angles = robot.imu.getAngularOrientation();
        float heading = -angles.firstAngle;
        bearing += Math.abs(Math.round(heading))-10;

        while(bearing > Math.abs(heading)) {
            angles = robot.imu.getAngularOrientation();
            heading = -angles.firstAngle;

            opMode.telemetry.addData("bearing", bearing);
            opMode.telemetry.addData("heading", heading);
            opMode.telemetry.addData("current angle bot is at", angles.firstAngle);
            opMode.telemetry.update();

           if(bearing < heading) {break;}
            move(0, 0, -1);

            if(opMode.isStopRequested()) {break;}
        }
        move(0, 0);
    }

    public void turnRight(int bearing) {
        robot.initIMU(opMode.hardwareMap);
        Orientation angles = robot.imu.getAngularOrientation();
        float heading = -angles.firstAngle;
        bearing += Math.round(heading)-10;

        while(bearing > heading) {
            angles = robot.imu.getAngularOrientation();
            heading = -angles.firstAngle;
            opMode.telemetry.addData("bearing", bearing);
            opMode.telemetry.addData("heading", heading);
            opMode.telemetry.addData("current angle bot is at", angles.firstAngle);
            opMode.telemetry.update();

            if(bearing < heading) {break;}
            move(0.7, -0.7);

            if(opMode.isStopRequested()) {break;}
        }
        move(0, 0);
    }

    public void encoderDrive(double speed, double leftCM, double rightCM, double timeoutS) {
        if(opMode.opModeIsActive()) {

            int newLeftTarget = robot.frontleft.getCurrentPosition() + (int)(leftCM*cmCount);
            int newRightTarget = robot.backright.getCurrentPosition() + (int)(rightCM*cmCount);

            robot.frontleft.setTargetPosition(newLeftTarget);
            robot.backright.setTargetPosition(newRightTarget);

            robot.frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.runtime.reset();
            robot.frontleft.setPower(Math.abs(speed));
            robot.backright.setPower(Math.abs(speed));

            while(opMode.opModeIsActive()
                && (robot.frontleft.isBusy() && robot.backright.isBusy())
                && (robot.runtime.seconds() < timeoutS)) {
            /*opMode.telemetry.addData("Target", "Running to %7d : %7d", newLeftTarget, newRightTarget);
            opMode.telemetry.addData("Current", "Running to %7d : %7d", robot.frontleft.getCurrentPosition(), robot.backright.getCurrentPosition());
            opMode.telemetry.update();

            if(leftCM < 0){
                robot.backleft.setPower(-speed);
                robot.frontright.setPower(-speed);
            } else if(leftCM >= 0){
                robot.backleft.setPower(speed);
                robot.frontright.setPower(speed);
            }

                }
            stop();

            opMode.sleep(250); //250 = 0.25 seconds
        }
    }*/
}