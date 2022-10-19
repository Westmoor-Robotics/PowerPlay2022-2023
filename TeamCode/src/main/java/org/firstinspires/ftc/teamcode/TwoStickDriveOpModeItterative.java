package org.firstinspires.ftc.teamcode;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Multiple Option Drive Mode")
public class TwoStickDriveOpModeItterative extends LinearOpMode {

    private DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private Servo leftServo, rightServo;
    private static double version = 1.3;
    private boolean twoStickDrive = false;

    @Override
    public void runOpMode() {
        // Setting Version (Just to detect build faillure)
        telemetry.addData("v",version);

        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftfrontmotor"); //in 2 drive config
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightfrontmotor"); // in 2 drive config
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftbackmotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightbackmotor");

        leftServo = hardwareMap.get(Servo.class,"leftservo");
        rightServo = hardwareMap.get(Servo.class,"rightservo");

        // IF WHEELS ARE SPINNING BACKWARDS CHANGE THESE TWO UP AND FIX IT PROBABLY MAYBE
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.FORWARD);




        waitForStart();

        while (opModeIsActive()) {
            boolean leftBumper = gamepad1.left_bumper;
            boolean rightBumper = gamepad1.right_bumper;

            double leftStick = -gamepad1.left_stick_y; // get left stick power
            double rightStick = (gamepad1.right_stick_x); // get right stick turn power. Right turn position = 1, left = -1

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

            twoStickDrive(leftStick,rightStick);

            telemetry.addData("Right Bumper", rightBumper);
            telemetry.addData("Left Bumper", leftBumper);

            telemetry.update();
        }
    }

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
        leftBackMotor.setPower(leftPower);
        telemetry.addData("Left Motor Power",leftPower);

        rightFrontMotor.setPower(rightPower);
        rightBackMotor.setPower(rightPower);
        telemetry.addData("Right Motor Power",rightPower);
    }

    private void regularDrive(double leftStick, double rightStick) {
        // Using passed stick amounts to set speeds of servos
        double leftPower = leftStick;
        double rightPower = rightStick;

        // Sets power
        leftFrontMotor.setPower(leftPower);
        leftBackMotor.setPower(leftPower);
    }


}
