//
//  QWFirstViewController.m
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import "QWRaceViewController.h"

@interface QWRaceViewController ()
@property NSOperationQueue *accelerometerQueue;
@property CMAcceleration lastAcceleration;
@property int steps;
@property int sendSteps;
@end

@implementation QWRaceViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
	
	self.accelerometerQueue = [[NSOperationQueue alloc] init];
	self.steps = 0;
	
	CMMotionManager *motionManager = [(QWAppDelegate *)[UIApplication sharedApplication].delegate motionManager];	//Get the global motion manager
	motionManager.accelerometerUpdateInterval = 0.2;	//Configure accelerometer rate
	[motionManager startAccelerometerUpdatesToQueue:self.accelerometerQueue withHandler:^(CMAccelerometerData *accelerometerData, NSError *error) {
		CMAcceleration acceleration = accelerometerData.acceleration;
		double xDelta = self.lastAcceleration.x - acceleration.x;
		double yDelta = self.lastAcceleration.y - acceleration.y;
		double zDelta = self.lastAcceleration.z - acceleration.z;
		double geometricDelta  = sqrt(pow(xDelta, 2)+pow(yDelta, 2)+pow(zDelta, 2));
		self.lastAcceleration = acceleration;
		if (geometricDelta > 2) {	//minimum threshold in G's (force of gravities)
			log_d(@"ACCEL",@"Step is happening! %d %f",self.steps, geometricDelta);
			self.steps++;
			dispatch_async(dispatch_get_main_queue(), ^{
				self.accelLabel.text = [NSString stringWithFormat:@"%d",self.steps];
				[self.accelLabel setNeedsDisplay];
				[self postSteps];
			});
		}
	}];
}

- (void)postSteps {
	
	
	QWAppDelegate *appDelegate = (QWAppDelegate *)[UIApplication sharedApplication].delegate;
	NSNumber *nextEventID = [appDelegate.nextEvent objectForKey:@"eventID"];
	NSNumber *userID = [appDelegate.user objectForKey:@"userID"];
	
	NSString *serverAddress = [NSString stringWithFormat:@"%@/events/%@/data",appDelegate.serverAddress,nextEventID];
	NSURL *serverURL = [NSURL URLWithString:serverAddress];
	NSMutableURLRequest *restRequest = [[NSMutableURLRequest alloc] initWithURL:serverURL];
	restRequest.HTTPMethod = @"POST";
	[restRequest setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
	[restRequest setValue:@"application/json" forHTTPHeaderField:@"Accept"];
	
	NSDictionary *update = @{@"userID":userID, @"movements":[NSNumber numberWithInteger:(self.steps-self.sendSteps)]};
	self.sendSteps = self.steps;
	
	NSError *error;
	NSData *jsonSerialization = [NSJSONSerialization dataWithJSONObject:update options:0 error:&error];
	if (error) {
		log_e(@"movement", @"Login JSON error %@",error.localizedDescription);
	} else {
		restRequest.HTTPBody = jsonSerialization;
		[NSURLConnection sendAsynchronousRequest:restRequest queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
			
			if (data) {
				NSError *error;
				NSDictionary *returnDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
				if (error) {
					log_e(@"movement", @"JSON return error %@",error.localizedDescription);
				} else {
					log_i(@"movement", @"moved successfully");
					if ([[returnDictionary objectForKey:@"complete"] boolValue]) {
						[self quitRace:nil];
					}
				}
			} else {
				log_e(@"movement", @"connection error: %@",connectionError.localizedDescription);
			}
		}];
	}
}

- (IBAction)resetSteps:(id)sender {
	self.steps = 0;
	[self postSteps];
	self.accelLabel.text = [NSString stringWithFormat:@"%d",self.steps];
	[self.accelLabel setNeedsDisplay];
}

- (IBAction)quitRace:(id)sender {
	[self.navigationController popViewControllerAnimated:YES];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
