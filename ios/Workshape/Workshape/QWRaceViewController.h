//
//  QWFirstViewController.h
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreMotion/CoreMotion.h>
#import "QWAppDelegate.h"

@interface QWRaceViewController : UIViewController

@property IBOutlet UILabel *accelLabel;

- (IBAction)resetSteps:(id)sender;
- (IBAction)quitRace:(id)sender;

@end
