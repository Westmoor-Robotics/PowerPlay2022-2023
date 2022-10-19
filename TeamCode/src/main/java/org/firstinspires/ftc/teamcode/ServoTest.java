package org.firstinspires.ftc.teamcode;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="ServoTest")
public class ServoTest extends LinearOpMode {
    private Servo leftServo, rightServo;
    private static double version = 1.0;

    @Override
    public void runOpMode() {
        // Setting Version (Just to detect build faillure)
        telemetry.addData("v",version);

        // Identifying Servo
        rightServo = hardwareMap.get(Servo.class,"rightservo");
        rightServo.setDirection(Servo.Direction.FORWARD);


        waitForStart();

        while (opModeIsActive()) {
            if(gamepad1.right_bumper) {
                rightServo.setPosition(0.8);
            } else {
                rightServo.setPosition(1);
            }

            telemetry.addData("Right Bumper", gamepad1.right_bumper);

            telemetry.update();
        }
    }
}
