//
//  QWSecondViewController.h
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <GoogleOpenSource/GoogleOpenSource.h>
#import <GooglePlus/GooglePlus.h>
#import "QWAppDelegate.h"

@interface QWSocialViewController : UIViewController <GPPSignInDelegate,FBLoginViewDelegate,UITextFieldDelegate>
@property IBOutlet UIImageView *loginCurtain;
@property IBOutlet UILabel *signInLabel;
@property IBOutlet UIButton *twitterLoginButton;
@property IBOutlet UIButton *twitterLogoutButton;
@property IBOutlet FBLoginView *facebookLoginButton;
@property IBOutlet GPPSignInButton *googleLoginButton;
@property IBOutlet UIButton *googleLogoutButton;
@property IBOutlet UIActivityIndicatorView *loginActivityIndicator;
@property IBOutlet UIButton *tryAgainButton;

- (IBAction)getTwitterPermissions:(id)sender;
//- (IBAction)getFacebookPermissions:(id)sender;
- (IBAction)logoutGoogle:(id)sender;
- (IBAction)login:(id)sender;
@end
