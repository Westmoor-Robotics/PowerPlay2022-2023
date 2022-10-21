package org.firstinspires.ftc.teamcode;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Servo Config Helper")
public class ServoConfigHelper extends LinearOpMode {
    private Servo leftServo, rightServo;
    private static double version = 1.1;

    @Override
    public void runOpMode() {
        // Setting Version (Just to detect build faillure)
        telemetry.addData("Servo Config v",version);

        // Identifying Servo
        rightServo = hardwareMap.get(Servo.class,"rightservo");
        rightServo.setDirection(Servo.Direction.FORWARD);

        leftServo = hardwareMap.get(Servo.class,"leftservo");
        leftServo.setDirection(Servo.Direction.REVERSE);

        double leftServoPos = 0.0;
        double rightServoPos = 0.0;

        waitForStart();

        while (opModeIsActive()) {
            leftServoPos = leftServoPos - gamepad1.left_stick_y;
            rightServoPos = rightServoPos - gamepad1.right_stick_y;

            leftServo.setPosition(leftServoPos);
            rightServo.setPosition(rightServoPos);

            telemetry.addData("Actual Left Servo Pos:", leftServo.getPosition());
            telemetry.addData("What the left servo pos should be", leftServoPos);
            telemetry.addData("The left stick value is", -gamepad1.left_stick_y);

            telemetry.addData("Actual Right Servo Pos:", rightServo.getPosition());
            telemetry.addData("What the right servo pos should be", rightServoPos);
            telemetry.addData("The right stick value is", -gamepad1.right_stick_y);
            telemetry.update();
        }
    }
}