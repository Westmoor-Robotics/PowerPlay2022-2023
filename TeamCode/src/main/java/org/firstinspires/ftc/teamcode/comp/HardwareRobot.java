package org.firstinspires.ftc.teamcode.comp;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;


public class HardwareRobot{
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor frontleft, backleft, frontright, backright, leftlift, rightlift, intakeleft, intakeright;
    public CRServo rightDuck, leftDuck;
    public Servo score;
    public DistanceSensor distSensor;
    public BNO055IMU imu;
    public static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    public static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };
    public static final String VUFORIA_KEY =
            "AcX0Evn/////AAABmWFY8cCdMUQ/h7KgNL5Lg7pxk1rbGYIJDyehQLUkUaW2p5ed1SPDz/Wnpio9ah27ATchi1bnQZnLzR7NimzcaPSzTYt/sL9Hj6vfjR9fKx2lQ3hP7C/C2ZnRGVSJWGFaKZIhjUlCQbO+j8Okv36Wvw44GaFcc+8q+7PLcp1z8HTA7qbQ5SFxDLcNNQaPKfYYGbfBYejduJKGiBSP+1d65R4C4Oc68mSZCzX6r6SDonLL0WKVWWm9X+kuxRuOqEGdKH7NdjYXywFZZREkOEZKbfn9I9SlvrVEi1WgAsPXGWbV9l0DOvGD76siCorP2A2N5h8DwD5wKDNxlMYNOUBO0JAAeHHPBOlxTKFgYmoLsTyG";
    //{@link #vuforia} is the variable we will use to store our instance of the Vuforia

    public VuforiaLocalizer vuforia;
    //{@link #tfod} is the variable we will use to store our instance of the TensorFlow Object

    public TFObjectDetector tfod;

    public void init(HardwareMap hwMap, Telemetry telemetry){

        frontleft = hwMap.get(DcMotor.class, "Front Left");
        backleft = hwMap.get(DcMotor.class, "Back Left");
        frontright = hwMap.get(DcMotor.class, "Front Right");
        backright = hwMap.get(DcMotor.class, "Back Right");

        leftlift = hwMap.get(DcMotor.class, "Left Lift");
        rightlift = hwMap.get(DcMotor.class, "Right Lift");

        intakeleft = hwMap.get(DcMotor.class, "Intake Left");
        intakeright = hwMap.get(DcMotor.class, "Intake Right");

        rightDuck = hwMap.get(CRServo.class, "Right Duck Servo");
        leftDuck = hwMap.get(CRServo.class, "Left Duck Servo");

        score = hwMap.get(Servo.class, "Score");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        frontleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightlift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setEncoders();
        setLiftEncoders();

        intakeleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //distSensor = hwMap.get(DistanceSensor.class, "Distance Sensor");


        initIMU(hwMap);
        initVuforia();
        initTfod(hwMap);
        /* Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0/9.0);
        }

        telemetry.addLine("Robot Ready");
        telemetry.update();
    }
    /*public double getDistanceCm(){
        return distSensor.getDistance(DistanceUnit.CM);
    }
    public double getDistanceInch(){
        return distSensor.getDistance(DistanceUnit.INCH);
    }*/

    public void setEncoders() {
        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setLiftEncoders() {
        rightlift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightlift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initVuforia() { //Initialize the Vuforia localization engine.
        //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    public void initTfod(HardwareMap hardwareMap) { //Initialize the TensorFlow Object Detection engine.
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.4f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    public void initIMU(HardwareMap hwMap) {
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.mode = BNO055IMU.SensorMode.IMU;
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = false;

        imu = hwMap.get(BNO055IMU.class, "imu");

        //i
        imu.initialize(params);
    }
}
