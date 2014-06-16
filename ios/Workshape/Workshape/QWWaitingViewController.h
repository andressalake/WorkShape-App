//
//  QWWaitingViewController.h
//  Workshape
//
//  Created by Kris on 2014-05-23.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QWWaitingViewController : UIViewController
@property IBOutlet UILabel *timeLabel;
@property IBOutlet UIButton *joinButton;
@property IBOutlet UIButton *leaveButton;


- (IBAction)joinRace:(id)sender;
- (IBAction)leaveRace:(id)sender;
- (IBAction)logout:(id)sender;

@end
