//
//  QWAvatarViewController.m
//  Workshape
//
//  Created by Kris on 2014-05-27.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import "QWAvatarViewController.h"
#import "QWAppDelegate.h"

@interface QWAvatarViewController ()
@property QWAppDelegate *appDelegate;
@end

@implementation QWAvatarViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
		self.appDelegate = (QWAppDelegate *)[UIApplication sharedApplication].delegate;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)selectAvatar:(id)sender {
	[self.appDelegate.user setObject:[NSNumber numberWithInt:[(UIImageView *)sender tag]] forKey:@"avatar"];
	[self performSegueWithIdentifier:@"selected" sender:self];
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
