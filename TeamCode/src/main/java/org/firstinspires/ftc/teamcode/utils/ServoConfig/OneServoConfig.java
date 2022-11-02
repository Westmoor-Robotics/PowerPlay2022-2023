package org.firstinspires.ftc.teamcode.utils.ServoConfig;


import static java.lang.Double.max;
import static java.lang.Double.min;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "ServoTestLast Resort", group = "TeleOp")
public class OneServoConfig extends LinearOpMode {
    Servo test;
    double pos = 0.5;
    boolean aPressed = false;
    boolean bPressed = false;
    boolean aReleased = true;
    boolean bReleased = true;
    boolean xPressed = false;
    boolean xReleased = false;
    boolean yPressed = false;
    boolean yReleased = false;
    boolean rightMotor = false;
    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            // "logic"
            if (gamepad1.back) {
                rightMotor = !rightMotor;
            }

            if (rightMotor) {
                test = hardwareMap.get(Servo.class, "rightservo");
            } else {
                test = hardwareMap.get(Servo.class, "leftservo");
            }

            if (gamepad1.a) {
                aPressed = aReleased;
                aReleased = false;
            } else {
                aPressed = false;
                aReleased = true;
            }
            if (gamepad1.b) {
                bPressed = bReleased;
                bReleased = false;
            } else {
                bPressed = false;
                bReleased = true;
            }
            if (gamepad1.x) {
                xPressed = xReleased;
                xReleased = false;
            } else {
                xPressed = false;
                xReleased = true;
            }
            if (gamepad1.y) {
                yPressed = yReleased;
                yReleased = false;
            } else {
                yPressed = false;
                yReleased = true;
            }
            if (aPressed) {
                pos = min(1, pos + 0.1);
            } else if (xPressed) {
                pos = min(1, pos + 0.01);
            } else if (bPressed) {
                pos = max(0, pos - 0.1);
            } else if (yPressed) {
                pos = max(0, pos - 0.01);
            }
            test.setPosition(pos);
            telemetry.addData("Position", pos);

            // this is terribly written lol
            if (rightMotor) {
                telemetry.addData("Right Motor Position",pos);
            } else {
                telemetry.addData("Left Motor Position", pos);
            }

            telemetry.update();
        }
    }
}
