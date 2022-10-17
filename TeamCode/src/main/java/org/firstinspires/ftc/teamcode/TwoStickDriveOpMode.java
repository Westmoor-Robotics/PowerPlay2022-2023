package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Two Stick Drive")
public class TwoStickDriveOpMode extends LinearOpMode {

    private DcMotor leftMotor, rightMotor;
    private Servo leftServo, rightServo;

    @Override
    public void runOpMode() {

        leftMotor = hardwareMap.get(DcMotor.class, "leftmotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightmotor");

        leftServo = hardwareMap.get(Servo.class,"leftservo");
        rightServo = hardwareMap.get(Servo.class,"rightservo");

        // IF WHEELS ARE SPINNING BACKWARDS CHANGE THESE TWO UP AND FIX IT PROBABLY MAYBE
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            boolean leftBumper = gamepad1.left_bumper;
            boolean rightBumper = gamepad1.right_bumper;

            double leftPower;
            double rightPower;

            double drivePower = -gamepad1.left_stick_y; // get left stick power
            double turnPower = 2 * (gamepad1.right_stick_x); // get right stick turn power. Right turn position = 1, left = -1

            // Just combining power of wheel (based off left stick power) to turn power to determine how much it should turn
            leftPower = Range.clip(drivePower - turnPower, -1.0, 1.0);
            rightPower = Range.clip(drivePower + turnPower, -1.0, 1.0);

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

            // GITUHB TEST


            // adjust pwoer
            leftMotor.setPower(leftPower);
            telemetry.addData("Left Motor Power",leftPower);
            rightMotor.setPower(rightPower);
            telemetry.addData("Right Motor Power",rightPower);
            telemetry.addData("Right Bumper", rightBumper);
            telemetry.addData("Left Bumper", leftBumper);
            telemetry.update();


        }
    }
}
