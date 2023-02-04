/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.AutonComp2022;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;

@Autonomous
public class RightAuton2022UsingEquation extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    Robot2022 robot = new Robot2022();
    Movement2022 move = new Movement2022(robot, this);

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT = 3;


    // Speed
    double x = 0.6;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(960,720, OpenCvCameraRotation.UPRIGHT);
                camera.showFpsMeterOnViewport(true);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        robot.init(hardwareMap, telemetry);
        move.closeServo();

        // Replaced Wait For Start
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */
        telemetry.update();
        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No Tag Found, how'd you manage to fuck this up?");
            telemetry.update();
        }


        if (!isStopRequested()) {

            tagToTelemetry(tagOfInterest);
            telemetry.update();


            // Voltage Readout
            double voltage = robot.vSensor.getVoltage();
            double calculatedPower;

            telemetry.addData("voltage is: ", voltage);
            telemetry.update();

            calculatedPower = (-0.05*voltage)+1.3;
            x = calculatedPower;


            // By now final power has been calculated and stored in X
            telemetry.addData("Running with a power of: ", x);
            telemetry.update();

            //Movement Code

            move.backward(0.65, x);
            move.stop(1);
            move.liftUp(1.1);
            move.strafeRight(0.65, x * 0.83);
            move.stop(1);
            move.forward(0.1, x);
            move.stop(1);
            move.openServo();
            sleep(2000);
            move.closeServo();
            move.backward(0.1, x);
            move.stop(1);
            move.liftDown(1.1);

            if (tagOfInterest != null) {
                parking(tagOfInterest);
            } else {
                telemetry.addData("No Tag Found", tagOfInterest);
            }

            move.stop(1);
        }
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            telemetry.addData("Left", tagOfInterest.id);
        }else if(tagOfInterest.id == MIDDLE){
            telemetry.addData("Middle", tagOfInterest.id);
        }else{
            telemetry.addData("Right", tagOfInterest.id);
        }
    }

    void parking(AprilTagDetection detection) {
        if(tagOfInterest.id == LEFT){
            leftPath();
        }else if(tagOfInterest.id == MIDDLE){
            midPath();
        }else if(tagOfInterest.id == RIGHT){
            rightPath();
        } else {
            telemetry.addData("Tag not found inside of parking methiod", tagOfInterest);
        }
    }

    void leftPath() {
        telemetry.addLine("Path Set to Left");
        telemetry.update();

        move.strafeRight(0.43, x);
        move.backward(0.2, x);
    }

    void rightPath() {
        telemetry.addLine("Path Set to Right");
        telemetry.update();

        move.strafeLeft(1.5, x);
    }

    void midPath() {
        telemetry.addLine("Path Set To Mid");
        telemetry.update();

        move.strafeLeft(0.4, x);
    }
}
