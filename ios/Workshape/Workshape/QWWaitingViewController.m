//
//  QWWaitingViewController.m
//  Workshape
//
//  Created by Kris on 2014-05-23.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import "QWWaitingViewController.h"
#import "QWAppDelegate.h"

@interface QWWaitingViewController ()
@property NSMutableDictionary *user;
@property NSDictionary *nextEvent;
@property NSTimer *countdownTimer;
@property BOOL joinedRace;
@end

@implementation QWWaitingViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	self.user = [(QWAppDelegate *)[UIApplication sharedApplication].delegate user];
	[self getNextRace];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)getNextRace {
	
	NSString *serverAddress = [NSString stringWithFormat:@"%@/events/next",[(QWAppDelegate *)[UIApplication sharedApplication].delegate serverAddress]];
	NSURL *serverURL = [NSURL URLWithString:serverAddress];
	NSMutableURLRequest *restRequest = [[NSMutableURLRequest alloc] initWithURL:serverURL];
	restRequest.HTTPMethod = @"GET";
	//[restRequest setValue:@"application/json" forHTTPHeaderField:@"Accept"];
	
	NSError *error;
	if (error) {
		log_e(@"events", @"Events JSON error %@",error.localizedDescription);
	} else {
		[NSURLConnection sendAsynchronousRequest:restRequest queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
			if (data) {
				NSError *error;
				NSDictionary *returnDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
				if (error) {
					log_e(@"events", @"Login JSON return error %@",error.localizedDescription);
				} else {
					log_i(@"events", @"got next event %@",returnDictionary);
					self.nextEvent = returnDictionary;
					[(QWAppDelegate *)[UIApplication sharedApplication].delegate setNextEvent:returnDictionary];
				}
			} else {
				log_e(@"events", @"Unable to get next event: %@ ",connectionError.localizedDescription);
			}
		}];
	}
	
	self.countdownTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(refreshTime) userInfo:nil repeats:YES];
}

- (IBAction)logout:(id)sender {
	[self.navigationController popToRootViewControllerAnimated:YES];
}

- (IBAction)joinRace:(id)sender {
	self.joinedRace = YES;
	self.joinButton.hidden = YES;
	self.leaveButton.hidden = NO;
}

- (IBAction)leaveRace:(id)sender {
	self.joinedRace = NO;
	self.joinButton.hidden = NO;
	self.leaveButton.hidden = YES;
}

- (IBAction)startRace:(id)sender {
	[self performSegueWithIdentifier:@"race" sender:self];
}

- (void)refreshTime {
	if (self.nextEvent) {
		NSDate *now = [NSDate date];
		 double eventTime = [[self.nextEvent objectForKey:@"time"] doubleValue]/1000;
		NSDate *eventDate = [NSDate dateWithTimeIntervalSince1970:eventTime];
		int delta = (int)[eventDate timeIntervalSinceDate:now];
		//NSDateComponents *components = [[NSCalendar currentCalendar] components:NSHourCalendarUnit fromDate:now toDate:eventDate options:0];
		
		int minutes = floor((delta%3600)/60);
		int seconds = delta%60;
		
		//if ((components.minute == 0) && (components.second == 0)) {
		//	[self performSegueWithIdentifier:@"race" sender:self];
		//}
		
		if ((minutes <= 0) && (seconds <= 0)) {
			[self.countdownTimer invalidate];
			
			if (self.joinedRace) {
				[self startRace:self];
			} else {
				[self getNextRace];
			}
		}
		
		
		//self.timeLabel.text = [NSString stringWithFormat:@"%02d:%02d",components.minute,components.second];

		self.timeLabel.text = [NSString stringWithFormat:@"%02d:%02d",minutes,seconds];
		[self.timeLabel setNeedsDisplay];
	}
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
