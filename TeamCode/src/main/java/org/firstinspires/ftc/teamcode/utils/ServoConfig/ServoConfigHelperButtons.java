package org.firstinspires.ftc.teamcode.utils.ServoConfig;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="Servo Config Helper Using D-Pad")
public class ServoConfigHelperButtons extends LinearOpMode {
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
            // Left Servo Updates
            if (gamepad1.dpad_up) {
                leftServoPos = leftServoPos + 0.1;
            }

            if (gamepad1.dpad_down) {
                leftServoPos = leftServoPos - 0.1;
            }

            // Right Servo Updates
            if (gamepad1.dpad_right) {
                rightServoPos = rightServoPos + 0.1;
            }

            if (gamepad1.dpad_left) {
                rightServoPos = rightServoPos - 0.1;
            }

            leftServo.setPosition(leftServoPos);
            rightServo.setPosition(rightServoPos);

            telemetry.addData("Actual Left Servo Pos:", leftServo.getPosition());
            telemetry.addData("Actual Right Servo Pos:", rightServo.getPosition());
            telemetry.update();
        }
    }
}
