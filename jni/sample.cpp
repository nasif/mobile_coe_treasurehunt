#include "com_tavant_mobilecoe_treasurehunt_camera_ComapareImage.h"
#include <stdio.h>
#include <iostream>
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "android/log.h"
#include "opencv2/imgproc/imgproc.hpp"  // Gaussian Blur
//#include "opencv2/nonfree/nonfree.hpp"


using namespace cv;
using namespace std;

double getPSNR ( const Mat& I1, const Mat& I2);
Scalar getMSSIM( const Mat& I1, const Mat& I2);
double histoCompare(const char*,const char*);
string retrieveString( const char* , int);

JNIEXPORT jdouble JNICALL Java_com_tavant_mobilecoe_treasurehunt_camera_ComapareImage_compareImage
  (JNIEnv  * env, jclass object,jstring originalfile, jstring fromcamera)
{

	const char *nativeOriginal = (env)->GetStringUTFChars(originalfile,0);
	const char *nativeCamera = (env)->GetStringUTFChars(fromcamera, 0);
	__android_log_print(ANDROID_LOG_ERROR, "TAG", "Files in native=%s:%s",  nativeOriginal, nativeCamera );

	//Mat img_1 = imread( "/mnt/sdcard/images/sachin_1.jpg", CV_LOAD_IMAGE_GRAYSCALE );
	//Mat img_2 = imread( "/mnt/sdcard/images/sachin_1.jpg", CV_LOAD_IMAGE_GRAYSCALE );
     /*
	if( !img_1.data || !img_2.data )
	{ std::cout<< " --(!) Error reading images " << std::endl; return ; }




	//-- Step 1: Detect the keypoints using SURF Detector
	int minHessian = 400;

	SurfFeatureDetector detector( minHessian );

	std::vector<KeyPoint> keypoints_1, keypoints_2;

    OrbFeatureDetector detector1;

	ORB orb;
	orb(img_1,Mat(),keypoints_1,detector1);



	detector.detect( img_1, keypoints_1 );
	detector.detect( img_2, keypoints_2 );

	//-- Step 2: Calculate descriptors (feature vectors)
	SurfDescriptorExtractor extractor;

	Mat descriptors_1, descriptors_2;

	extractor.compute( img_1, keypoints_1, descriptors_1 );
	extractor.compute( img_2, keypoints_2, descriptors_2 );

	//-- Step 3: Matching descriptor vectors using FLANN matcher
	FlannBasedMatcher matcher;
	std::vector< DMatch > matches;
	matcher.match( descriptors_1, descriptors_2, matches );

	double max_dist = 0; double min_dist = 100;

	//-- Quick calculation of max and min distances between keypoints
	for( int i = 0; i < descriptors_1.rows; i++ )
	{ double dist = matches[i].distance;
	if( dist < min_dist ) min_dist = dist;
	if( dist > max_dist ) max_dist = dist;
	}

	printf("-- Max dist : %f \n", max_dist );
	printf("-- Min dist : %f \n", min_dist );

	//-- Draw only "good" matches (i.e. whose distance is less than 2*min_dist )
	//-- PS.- radiusMatch can also be used here.
	std::vector< DMatch > good_matches;

	for( int i = 0; i < descriptors_1.rows; i++ )
	{ if( matches[i].distance <= 2*min_dist )
	{ good_matches.push_back( matches[i]); }
	}

	//-- Draw only "good" matches
	Mat img_matches;
	drawMatches( img_1, keypoints_1, img_2, keypoints_2,
			good_matches, img_matches, Scalar::all(-1), Scalar::all(-1),
			vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS );

	//-- Show detected matches
	imshow( "Good Matches", img_matches );

	for( int i = 0; i < good_matches.size(); i++ )
	{
		printf( "-- Good Match [%d] Keypoint 1: %d  -- Keypoint 2: %d  \n", i, good_matches[i].queryIdx, good_matches[i].trainIdx );
	}

   */

     /*

	double psnr=getPSNR(img_1,img_2);
	Scalar scalar=getMSSIM(img_1,img_2);
	 __android_log_print(ANDROID_LOG_ERROR, "TAG",  "psnr is =%lf",psnr);
	 __android_log_print(ANDROID_LOG_ERROR, "TAG",  "Calculating mse");
	 //__android_log_print(ANDROID_LOG_ERROR, "TAG",  "mse is =%lf",std::string(scalar));
	  *
	  */
	double correlaton=histoCompare(nativeOriginal,nativeCamera);
	__android_log_print(ANDROID_LOG_ERROR, "TAG", "correlaton : %f\n", correlaton);
	 env->ReleaseStringUTFChars(originalfile, nativeOriginal);
	 env->ReleaseStringUTFChars(fromcamera, nativeCamera);
	return (jdouble)correlaton;
}






double histoCompare(const char *originalfile,const char *fromcamera){

	  Mat src_test1, hsv_test1;
	  Mat src_test2, hsv_test2;

	  String s1=retrieveString(originalfile,strlen(originalfile));
	  String s2=retrieveString(fromcamera,strlen(fromcamera));


	  __android_log_print(ANDROID_LOG_ERROR, "TAG", "CPP string after comparison%s",  s1.c_str());
	  __android_log_print(ANDROID_LOG_ERROR, "TAG", "CPP string after comparison%s",  s2.c_str());

	  src_test1 = imread(s1,1);
	  src_test2 = imread(s2, 1 );

	  /// Convert to HSV
	  cvtColor( src_test1, hsv_test1, CV_BGR2HSV );
	  cvtColor( src_test2, hsv_test2, CV_BGR2HSV );



	  /// Using 30 bins for hue and 32 for saturation
	  int h_bins = 50; int s_bins = 60;
	  int histSize[] = { h_bins, s_bins };

	  // hue varies from 0 to 256, saturation from 0 to 180
	  float h_ranges[] = { 0, 256 };
	  float s_ranges[] = { 0, 180 };

	  const float* ranges[] = { h_ranges, s_ranges };

	  // Use the o-th and 1-st channels
	  int channels[] = { 0, 1 };

	  /// Histograms

	  MatND hist_test1;
	  MatND hist_test2;



	  calcHist( &hsv_test1, 1, channels, Mat(), hist_test1, 2, histSize, ranges, true, false );
	  normalize( hist_test1, hist_test1, 0, 1, NORM_MINMAX, -1, Mat() );

	  calcHist( &hsv_test2, 1, channels, Mat(), hist_test2, 2, histSize, ranges, true, false );
	  normalize( hist_test2, hist_test2, 0, 1, NORM_MINMAX, -1, Mat() );

	  /// Apply the histogram comparison methods
	  double correlaton=0.0;
	  for( int i = 0; i < 4; i++ )
	     {
		  int compare_method = i;
	       double base_test1 = compareHist( hist_test1, hist_test2, compare_method );
	       if(i==0)
	       correlaton=base_test1;
	       double base_test2 = compareHist( hist_test2, hist_test1, compare_method );
	       __android_log_print(ANDROID_LOG_ERROR, "TAG", "Base-Test(1), Base-Test(2) : %f, %f \n",  base_test1, base_test2 );
	     }

	  printf( "Done \n" );

	  return correlaton;
}

string retrieveString( const char* buf, int max ) {
    size_t len = 0;
    while( (len < max) && (buf[ len ] != '\0') ) {
        len++;
    }

    return string( buf, len );
}
