// autonomous program that drives bot forward a set distance, stops then
// backs up to the starting point using encoders to measure the distance.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Drive Encoder", group="Exercises")
//@Disabled
public class encoderExperiment extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("Front Left");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("Back Left");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("Front Right");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("Back Right");

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // reset encoder counts kept by motors.
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        // run mode
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set motors to run forward for 5000 encoder counts.
        motorFrontLeft.setTargetPosition(5000);
        motorFrontRight.setTargetPosition(5000);
        motorBackLeft.setTargetPosition(5000);
        motorBackRight.setTargetPosition(5000);

        // set motors to run to target encoder position and stop with brakes on.
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        // set both motors to 25% power. Movement will start. Sign of power is
        // ignored as sign of target encoder position controls direction when
        // running to position.

        motorFrontLeft.setPower(0.1);
        motorFrontRight.setPower(0.1);
        motorBackLeft.setPower(0.1);
        motorBackRight.setPower(0.1);

        // wait while opmode is active and left motor is busy running to position.

        while (opModeIsActive() && motorFrontLeft.isBusy() && !isStopRequested() && motorFrontLeft.getCurrentPosition() > -5000)   //leftMotor.getCurrentPosition() < leftMotor.getTargetPosition())
        {
            telemetry.addData("encoder-fwd-left", motorFrontLeft.getCurrentPosition() + "  busy=" + motorFrontLeft.isBusy());
            telemetry.addData("encoder-fwd-right", motorFrontRight.getCurrentPosition() + "  busy=" + motorFrontRight.isBusy());

            telemetry.addData("encoder-rwd-left", motorBackLeft.getCurrentPosition() + "  busy=" + motorBackLeft.isBusy());
            telemetry.addData("encoder-rwd-right", motorBackRight.getCurrentPosition() + "  busy=" + motorBackRight.isBusy());

            telemetry.addData("FR", motorFrontRight.getPower());
            telemetry.addData("FL", motorFrontLeft.getPower());
            telemetry.addData("BR", motorBackRight.getPower());
            telemetry.addData("BL", motorBackLeft.getPower());

            telemetry.update();
            idle();
        }

        // set motor power to zero to turn off motors. The motors stop on their own but
        // power is still applied so we turn off the power.

        motorFrontLeft.setPower(0.0);
        motorFrontRight.setPower(0.0);
        motorBackLeft.setPower(0.0);
        motorBackRight.setPower(0.0);

        // wait 5 sec to you can observe the final encoder position.

        resetRuntime();

        while (opModeIsActive() && getRuntime() < 5)
        {
            telemetry.addData("encoder-fwd-left-end", motorFrontLeft.getCurrentPosition());
            telemetry.addData("encoder-fwd-right-end", motorFrontRight.getCurrentPosition());

            telemetry.addData("encoder-rwd-left-end", motorBackLeft.getCurrentPosition());
            telemetry.addData("encoder-rwd-right-end", motorBackRight.getCurrentPosition());
            telemetry.update();

            idle();
        }
    }
}