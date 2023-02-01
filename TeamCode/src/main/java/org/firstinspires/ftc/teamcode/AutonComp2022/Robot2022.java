package org.firstinspires.ftc.teamcode.AutonComp2022;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;


public class Robot2022 {
    public ElapsedTime runtime = new ElapsedTime();

    public DcMotor frontleft, backleft, frontright, backright, rightlift, leftlift;
    public ColorSensor color_sensor;
    public Servo leftGrab, rightGrab;


    public BNO055IMU imu;

    public void init(HardwareMap hwMap, Telemetry telemetry){

        frontleft = hwMap.get(DcMotor.class, "Front Left");
        backleft = hwMap.get(DcMotor.class, "Back Left");
        frontright = hwMap.get(DcMotor.class, "Front Right");
        backright = hwMap.get(DcMotor.class, "Back Right");
        rightlift = hwMap.get(DcMotor.class, "Right Lift Motor");
        leftlift = hwMap.get(DcMotor.class, "Left Lift Motor");

        color_sensor = hwMap.get(ColorSensor.class, "Color");

        leftGrab = hwMap.get(Servo.class, "Left Grab");
        rightGrab = hwMap.get(Servo.class, "Right Grab");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backright.setDirection(DcMotor.Direction.REVERSE);
        rightlift.setDirection(DcMotor.Direction.FORWARD);
        leftlift.setDirection(DcMotor.Direction.REVERSE);

        frontleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //setEncoders();
        initIMU(hwMap);
        runtime.reset();
        telemetry.addLine("Robot Ready");
        telemetry.update();
    }

    public void setEncoders() {
        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftlift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void setLiftEncoders() {
        leftlift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightlift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftlift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightlift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initIMU(HardwareMap hwMap) {
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.mode = BNO055IMU.SensorMode.IMU;
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = false;

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);
    }
}