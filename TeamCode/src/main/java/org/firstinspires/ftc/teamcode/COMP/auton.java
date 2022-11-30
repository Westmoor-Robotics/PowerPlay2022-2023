package org.firstinspires.ftc.teamcode.COMP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous
public class auton extends LinearOpMode {
    ColorSensor color;

    @Override
    public void runOpMode() throws InterruptedException {

        color = hardwareMap.colorSensor.get("color");
        double blue;
        double red;
        double green;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            blue = color.blue();
            red = color.red();
            green = color.green();

            telemetry.addData("Blue", blue);
            telemetry.addData("Green", green);
            telemetry.addData("Red", red);
        }
    }
}