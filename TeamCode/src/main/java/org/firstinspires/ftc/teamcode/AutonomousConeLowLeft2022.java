package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutonComp2022.Movement2022;
import org.firstinspires.ftc.teamcode.AutonComp2022.Robot2022;

@Autonomous(name="AutonomousConeLowLeft2022", group="Linear Opmode")
public class AutonomousConeLowLeft2022 extends LinearOpMode {

    Robot2022 robot = new Robot2022();
    Movement2022 move = new Movement2022(robot, this);

    public void runOpMode() {
        robot.init(hardwareMap, telemetry);
        move.closeServo();

        waitForStart();

        int position = 1; // put scan function here

        double x = 0.6;

        move.backward(0.65, x);
        move.stop(1);
        move.liftUp(1.1);
        move.strafeLeft(0.5, x);
        move.stop(1);
        move.forward(0.1, x);
        move.stop(1);
        move.openServo();
        sleep(2000);
        move.closeServo();
        move.backward(0.1, x);
        move.stop(1);
        move.liftDown(1.1);

        if(position == 1) {
            move.strafeRight(1.5, x);

        }

        if(position == 2) {
            move.strafeRight(0.4, x);
        }

        if(position == 3) {
            move.strafeLeft(0.4, x);
        }
        move.stop(1);
    }}