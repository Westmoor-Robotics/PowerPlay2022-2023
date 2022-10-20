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
    private static double version = 1.0;

    @Override
    public void runOpMode() {
        // Setting Version (Just to detect build faillure)
        telemetry.addData("v",version);

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

            telemetry.addData("Left Servo Pos:", leftServo.getPosition());
            telemetry.addData("Right Servo Pos:", rightServo.getPosition());
            telemetry.update();
        }
    }
}
