package org.firstinspires.ftc.teamcode.Misc.TrunkOrTreat;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Multiple Option Drive Mode With Only Two Motors")
public class MultiDriveModeTwoMotors extends LinearOpMode {

    private DcMotor leftFrontMotor, rightFrontMotor;
    private Servo leftServo, rightServo;
    private static double version = 1.3;
    private boolean twoStickDrive = false;



    @Override
    public void runOpMode() {
        // Setting Version (Just to detect build failure)
        telemetry.addData("v",version);

        // Hardware maps!!!
        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftmotor"); //in 2 drive config
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightmotor"); // in 2 drive config

        leftServo = hardwareMap.get(Servo.class,"leftservo");
        rightServo = hardwareMap.get(Servo.class,"rightservo");

        // Motor Direction Config
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);

        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            // Control Variables
            boolean leftBumper = gamepad1.left_bumper;
            boolean rightBumper = gamepad1.right_bumper;

            double leftStick = -gamepad1.left_stick_y; // get left stick power
            double rightStick = (gamepad1.right_stick_x); // get right stick turn power. Right turn position = 1, left = -1

            // Servo Control (NEED DEBUGGING)
            if(leftBumper) {
                leftServo.setPosition(1);
            } else {
                leftServo.setPosition(0);
            }

            if(rightBumper) {
                rightServo.setPosition(1);
            } else {
                rightServo.setPosition(0);
            }

            // Adjusts the drive mode state if A button pressed
            if (gamepad1.a){
                twoStickDrive = !twoStickDrive;
            }

            // Appropriately sets drive mode and calls
            if (twoStickDrive) {
                twoStickDrive(leftStick,rightStick);
            } else {
                regularDrive(leftStick,rightStick);
            }

            // Telemetry Updates
            telemetry.addData("Right Bumper", rightBumper);
            telemetry.addData("Left Bumper", leftBumper);
            telemetry.addData("Two Stick Drive:", twoStickDrive);
            telemetry.update();
        }
    }

    // Drive mode that uses left stick to accel/deccel and right to turn
    private void twoStickDrive(double leftStick, double rightStick) {
        telemetry.addData("If You're Seeing Two Stick Drive Method Works", version);

        double leftPower;
        double rightPower;

        if (leftStick == 1 && rightStick == -1) {
            rightStick = rightStick * 2;
        }

        // Just combining power of wheel (based off left stick power) to turn power to determine how much it should turn
        leftPower = Range.clip(leftStick - rightStick, -1.0, 1.0);
        rightPower = Range.clip(leftStick + rightStick, -1.0, 1.0);

        // Setting Power Appropriately
        leftFrontMotor.setPower(leftPower);
        telemetry.addData("Left Motor Power",leftPower);

        rightFrontMotor.setPower(rightPower);
        telemetry.addData("Right Motor Power",rightPower);
    }

    // Traditional drive mode that utilizes left stick for left motors and right stick for right motors
    private void regularDrive(double leftStick, double rightStick) {
        // Using passed stick amounts to set speeds of servos
        double leftPower = leftStick;
        double rightPower = rightStick;

        // Sets power
        leftFrontMotor.setPower(leftPower);
        rightFrontMotor.setPower(rightPower);
    }
}
