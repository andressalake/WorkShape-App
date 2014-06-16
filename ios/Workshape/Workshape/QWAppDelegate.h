//
//  QWAppDelegate.h
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreMotion/CoreMotion.h>
#import "QWLog.h"

@interface QWAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
//@property CMStepCounter *stepCounter;	//Only iPhone 5S can do this, so maybe we shouldn't rely on it
@property CMMotionManager *motionManager;
@property NSMutableDictionary *user;
@property NSDictionary *nextEvent;
@property NSString *serverAddress;

- (void)loginAndPerformSelector:(SEL)selector withObject:(id)target;
- (void)acceptInvitation:(NSString *)inviteID;

@end
