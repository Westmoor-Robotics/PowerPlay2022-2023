package org.firstinspires.ftc.teamcode.Misc.ServoConfig;

// botched together by: fedor khaldin
// if doesn't work ask for support: anyone but fedor khaldin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.checkerframework.checker.units.qual.min;


@TeleOp(name="Servo Config Helper Using Sticks")
public class ServoConfigHelperSticks extends LinearOpMode {
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

        double previousLeftPos = 0.0;
        double previousRightPos = 0.0;

        waitForStart();

        while (opModeIsActive()) {

           double leftStick = -0.005 * gamepad1.left_stick_y;
           double rightStick = -0.005 * gamepad1.right_stick_y;

           if (previousLeftPos != leftStick) {
               leftServoPos = Range.clip(leftServoPos + leftStick, 0, 1);
               previousLeftPos = leftServo.getPosition();
           }

           if (previousRightPos != rightStick) {
               rightServoPos = Range.clip(rightServoPos + rightStick, 0, 1);
               previousRightPos = rightServo.getPosition();
           }

            leftServo.setPosition(leftServoPos);
            rightServo.setPosition(rightServoPos);

            telemetry.addData("Actual Left Servo Pos:", leftServo.getPosition());
            telemetry.addData("The left stick value is", leftStick);

            telemetry.addData("Actual Right Servo Pos:", rightServo.getPosition());
            telemetry.addData("The right stick value is", rightStick);
            telemetry.update();
        }
    }
}
