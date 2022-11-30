package org.firstinspires.ftc.teamcode.Misc.PreviousSeasonCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.List;


public class Gesticulation {
    HardwareRobot robot = null;
    LinearOpMode opMode = null;





    double power = 1;
    double cmCount = 15.5;







    //practice bot: 28.169 ticks per cm, comp bot: 15.5 ticks per cm
    Gesticulation(HardwareRobot arobot, LinearOpMode aOpMode){
        robot = arobot;
        opMode = aOpMode;
    }

    public void stop(long x){
        move(0,0);
        opMode.telemetry.addData("direction","urmom");
        opMode.telemetry.update();
        opMode.sleep(x);
    }

    public void stop(){
        move(0,0);
    }

    public void move(double leftspeed, double rightspeed){
        robot.frontleft.setPower(leftspeed);
        robot.backleft.setPower(leftspeed);
        robot.frontright.setPower(rightspeed);
        robot.backright.setPower(rightspeed);
    }

    public void turnLeft(int bearing){
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
            move(-0.7, 0.7);

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
            opMode.telemetry.update();*/

                if(leftCM < 0){
                    robot.backleft.setPower(-speed);
                    robot.frontright.setPower(-speed);
                } else if(leftCM >= 0){
                    robot.backleft.setPower(speed);
                    robot.frontright.setPower(speed);
                }

            }
            stop();

            robot.setEncoders();

            opMode.sleep(250); //250 = 0.25 seconds
        }
    }

    public void liftDrive(int tikCount, double timeoutS) {
        if(opMode.opModeIsActive()) {

            int newLeftTarget = -tikCount;
            int newRightTarget = tikCount;

            robot.leftlift.setTargetPosition(newLeftTarget);
            robot.rightlift.setTargetPosition(newRightTarget);

            robot.leftlift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightlift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.runtime.reset();
            robot.leftlift.setPower(0.6);
            robot.rightlift.setPower(0.6);

            while(opMode.opModeIsActive()
                    && (robot.leftlift.isBusy() &&robot.rightlift.isBusy())
                    && (robot.runtime.seconds() < timeoutS)) {
            /*opMode.telemetry.addData("Target", "Running to %7d : %7d", newLeftTarget, newRightTarget);
            opMode.telemetry.addData("Current", "Running to %7d : %7d", robot.frontleft.getCurrentPosition(), robot.backright.getCurrentPosition());
            opMode.telemetry.update();
                opMode.telemetry.addData("seconds", robot.runtime.seconds());
                opMode.telemetry.update();*/
            }

            robot.leftlift.setPower(0);
            robot.rightlift.setPower(0);

            opMode.sleep(250); //250 = 0.25 seconds
        }
    }

    public void encoderDrive(double speed, double cm, double timeOutS) {
        encoderDrive(speed, cm, cm, timeOutS);
    }
    public void spinDuck(int sec){
        robot.rightDuck.setPower(-1);
        robot.leftDuck.setPower(-1);
        opMode.sleep(sec*1000);
        robot.rightDuck.setPower(0);
        robot.leftDuck.setPower(0);
    }
    public void spinDuckCC(int sec){
        robot.rightDuck.setPower(-1);
        robot.leftDuck.setPower(-1);
        opMode.sleep(sec*1000);
        robot.rightDuck.setPower(0);
        robot.leftDuck.setPower(0);
    }
    public void scoreCube(){
        robot.score.setPosition(0);
        opMode.sleep(2000);
        robot.score.setPosition(0.3);
    }
    public int scan(){
        int tikcount= 0;
        if (opMode.opModeIsActive()) {
            robot.runtime.reset();
            while (opMode.opModeIsActive() && (robot.runtime.seconds() < 2)) {
                if (robot.tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            i++;

                            //midpoint
                            double midx = (recognition.getLeft() + recognition.getRight())/2;
                            double midy = (recognition.getHeight() + recognition.getBottom())/2;

                            if(midx <= 400) {
                                tikcount = 0;
                            } else if (midx >= 400 && midx <= 800) {
                                tikcount = 600;
                            } else if (midx > 800) {
                                tikcount = 1200;
                            }
                        }
                    }
                }
            }
        }
        return tikcount;
    }
}

/*    public void forward(long x){
            move(power, power);
            opMode.telemetry.addData("direction","forward");
            opMode.telemetry.update();
            opMode.sleep(x);
    }

        public void turnleft(long x){
            move(power, -power);
            opMode.telemetry.addData("direction","left");
            opMode.telemetry.update();
            opMode.sleep(x);

        }
        public void turnright(long x){
            move(-power, power);
            opMode.telemetry.addData("direction","right");
            opMode.telemetry.update();
            opMode.sleep(x);
        }
        public void backward(long x){
            move(-power, -power);
            opMode.telemetry.addData("direction","backward");
            opMode.telemetry.update();
            opMode.sleep(x);
        }
        */
