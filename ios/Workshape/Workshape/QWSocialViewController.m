//
//  QWSecondViewController.m
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import "QWSocialViewController.h"
#import <Accounts/Accounts.h>
#import "QWLog.h"

@interface QWSocialViewController ()
@property NSMutableDictionary *user;
@property ACAccountStore *accountStore;
@property ACAccount *twitterAccount;
@property QWAppDelegate *appDelegate;
@property BOOL showButtons;
@end

@implementation QWSocialViewController

static NSString *const kClientId = @"350435369758-jps3bculshfrpjpftgdv9da6fh9h4fdu.apps.googleusercontent.com";	//Google App Client ID

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
	self.appDelegate = (QWAppDelegate *)[UIApplication sharedApplication].delegate;
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(login:) name:@"login" object:nil];
		
	self.user = self.appDelegate.user;
	
	//For Twitter
	self.accountStore = [[ACAccountStore alloc] init];
	ACAccountType *twitterType = [self.accountStore accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierTwitter];
	self.twitterAccount = [[self.accountStore accountsWithAccountType:twitterType] lastObject];
	if (self.twitterAccount) {
		[self login:nil];
	}
	//[self refreshTwitterButton:(self.twitterAccount != nil)];
	
	//For Facebook
	self.facebookLoginButton.readPermissions = @[@"public_profile",@"email",@"user_friends"];
	
	//For Google
	// https://developers.google.com/+/mobile/ios/sign-in
	GPPSignIn *googleSignIn = [GPPSignIn sharedInstance];
	googleSignIn.shouldFetchGooglePlusUser = YES;
	googleSignIn.scopes = @[kGTLAuthScopePlusLogin];
	googleSignIn.clientID = kClientId;
	googleSignIn.delegate = self;
	
	[googleSignIn trySilentAuthentication];	//see if we're already signed in
	
	//self.loginCurtain.frame = CGRectMake(20.0, 160.0, self.loginCurtain.frame.size.width, self.loginCurtain.frame.size.height);
	[self performSelector:@selector(hideCurtain) withObject:self afterDelay:7.0];
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)login:(id)object {
	log_i(@"login", @"logging in now...");
	if (!self.loginActivityIndicator.isAnimating) {
		[self.loginActivityIndicator startAnimating];
		self.tryAgainButton.hidden = YES;
		
		[self.appDelegate loginAndPerformSelector:@selector(loginFinishedWithError:) withObject:self];
	}
}

- (void)loginFinishedWithError:(NSError *)error {
	[self.loginActivityIndicator stopAnimating];
	if (error) {
		self.tryAgainButton.hidden = NO;
		UIAlertView *errorAlert = [[UIAlertView alloc] initWithTitle:error.localizedDescription message:error.localizedRecoverySuggestion delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[errorAlert show];
	} else {
		log_i(@"login", @"logged in successfully %@",[self.user objectForKey:@"userID"]);
		NSMutableArray *companies = [self.user objectForKey:@"companies"];
		if (self.navigationController.viewControllers.lastObject == self) {
			if (companies.count > 0) {
				[self performSegueWithIdentifier:@"invited" sender:self];
			} else {
				[self performSegueWithIdentifier:@"uninvited" sender:self];
			}
		}
	}
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
	self.appDelegate.serverAddress = [NSString stringWithFormat:@"http://%@",textField.text];
}


- (void)hideCurtain {
	self.signInLabel.alpha = 0;
	self.signInLabel.hidden = NO;
	self.twitterLoginButton.alpha = 0;
	self.twitterLoginButton.hidden = NO;
	self.facebookLoginButton.alpha = 0;
	self.facebookLoginButton.hidden = NO;
	self.googleLoginButton.alpha = 0;
	self.googleLoginButton.hidden = NO;
	
	
	[UIView beginAnimations:nil context:nil];
	[UIView setAnimationDuration:0.5];
	[UIView setAnimationDelay:0];
	//[self.loginCurtain setFrame:CGRectMake(20.0, 20.0, self.loginCurtain.frame.size.width, self.loginCurtain.frame.size.height)];
	self.showButtons = YES;
	self.signInLabel.alpha = 1;
	self.twitterLoginButton.alpha = 1;
	self.facebookLoginButton.alpha = 1;
	self.googleLoginButton.alpha = 1;
	//self.loginCurtain.hidden = YES;
	[UIView commitAnimations];
}

#pragma mark Twitter

- (void)refreshTwitterButton:(BOOL)isAuthenticated {
	if (isAuthenticated) {
		[self.twitterLogoutButton setTitle:[NSString stringWithFormat:@"Logged in as %@",self.twitterAccount.accountDescription] forState:UIControlStateNormal];
		self.twitterLoginButton.hidden = YES;
		self.twitterLogoutButton.hidden = NO | !self.showButtons;
	} else {
		self.twitterLoginButton.hidden = NO;
		self.twitterLogoutButton.hidden = YES | !self.showButtons;
	}
	[self.twitterLoginButton setNeedsDisplay];
	[self.twitterLogoutButton setNeedsDisplay];
}

- (IBAction)getTwitterPermissions:(id)sender {
	ACAccountType *twitterType = [self.accountStore accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierTwitter];
	[self.accountStore requestAccessToAccountsWithType:twitterType options:nil completion:^(BOOL granted, NSError *error) {
		if (granted) {
			if (self.accountStore.accounts.count > 0) {
				self.twitterAccount = self.accountStore.accounts.lastObject;
				log_i(@"Accounts", @"Twitter Permission Success! %@",self.twitterAccount.accountDescription);
				
				[self.user setObject:self.twitterAccount.accountDescription forKey:@"twitterUID"];
				//get twitter token?  Reverse Auth required
				
				[self refreshTwitterButton:granted];
				[self login:nil];
			} else {
				log_w(@"Accounts", @"No twitter accounts found");
				UIAlertView *noAccountsAlert = [[UIAlertView alloc] initWithTitle:@"No Accounts Found" message:@"Configure Twitter in the Settings app" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
				[noAccountsAlert performSelectorOnMainThread:@selector(show) withObject:nil waitUntilDone:NO];
			}
		} else {
			log_e(@"Accounts", @"Account access denied: %@", error.localizedDescription);
			UIAlertView *unauthorizedAlert = [[UIAlertView alloc] initWithTitle:@"Permission Denied" message:@"Please enable access in the Settings App" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[unauthorizedAlert performSelectorOnMainThread:@selector(show) withObject:nil waitUntilDone:NO];
		}
	}];
}

- (IBAction)logoutTwitter:(id)sender {
	[self.accountStore removeAccount:self.twitterAccount withCompletionHandler:^(BOOL success, NSError *error) {
		if (success) {
			[self refreshTwitterButton:!success];
		} else {
			log_e(@"Accounts", @"Account removal denied: %@",error);
			UIAlertView *unauthorizedAlert = [[UIAlertView alloc] initWithTitle:@"Unable to Logout" message:@"Please disable access in the Settings App" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[unauthorizedAlert performSelectorOnMainThread:@selector(show) withObject:nil waitUntilDone:NO];
		}
	}];
}

#pragma mark Facebook

//Used for built-in iOS Facebook support.  We use Facebook's framework instead.
//- (IBAction)getFacebookPermissions:(id)sender {
//	ACAccountType *facebookType = [self.accountStore accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierFacebook];
//	NSDictionary *facebookOptions = @{ACFacebookAppIdKey: @"012345678912345", ACFacebookPermissionsKey:@[@"public_profile",@"email",@"user_friends"]};
//	[self.accountStore requestAccessToAccountsWithType:facebookType options:facebookOptions completion:^(BOOL granted, NSError *error) {
//		if (granted) {
//			log_i(@"Accounts", @"Facebook Permission Success! %d account(s)",self.accountStore.accounts.count);
//		} else {
//			log_e(@"Accounts", @"Account access denied: %@", error.localizedDescription);
//		}
//	}];
//}

- (void)loginView:(FBLoginView *)loginView handleError:(NSError *)error {
	log_e(@"facebook", [error localizedDescription]);
}

- (void)loginViewShowingLoggedInUser:(FBLoginView *)loginView {
	log_d(@"facebook", @"showing logged-in user");
}

- (void)loginViewShowingLoggedOutUser:(FBLoginView *)loginView {
	log_d(@"facebook", @"showing logged-out user");
}

- (void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user {
	log_i(@"facebook", @"login user info! %@ %@",user.id,user.name);
	[self.user setObject:user.id forKey:@"fbUID"];
	//[self.user setObject:[FBSession activeSession].accessTokenData forKey:@"fbToken"];
	[self login:nil];
}


#pragma mark Google

- (void)refreshGoogleButton:(GTMOAuth2Authentication *)isAuthorized {
	//Hide the button, we're good
	if (isAuthorized) {
		self.googleLoginButton.hidden = YES;
		self.googleLogoutButton.hidden = NO | !self.showButtons;
	} else {
		self.googleLoginButton.hidden = NO | !self.showButtons;
		self.googleLogoutButton.hidden = YES;
	}
	[self.googleLoginButton setNeedsDisplay];
	[self.googleLogoutButton setNeedsDisplay];
}

- (void)finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error {	//Google
	if (error) {
		NSLog(@"Received error %@ and auth object %@", [error localizedDescription], auth);
	} else {
		//Google Auth success
		//[self.user setObject:auth.accessToken forKey:@"googleToken"];
		
		//maybe get some user info?
		GTLServicePlus *googlePlusService = [[GTLServicePlus alloc] init];
		[googlePlusService setAuthorizer:auth];
		GTLQueryPlus *userQuery = [GTLQueryPlus queryForPeopleGetWithUserId:@"me"];
		[googlePlusService executeQuery:userQuery completionHandler:^(GTLServiceTicket *ticket, GTLPlusPerson *person, NSError *error) {
			if (error) {
				log_e(@"google", @"user get error %@",[error localizedDescription]);
			} else {
				//log_i(@"google",@"got some user id goodness! %@",person);
				[self.user setObject:person.identifier forKey:@"googleUID"];
				[self login:nil];
			}
		}];
		[self refreshGoogleButton:auth];
	}
}

- (void)logoutGoogle:(id)sender {
	[[GPPSignIn sharedInstance] signOut];
	[self refreshGoogleButton:[[GPPSignIn sharedInstance] authentication]];
}

- (void)presentSignInViewController:(UIViewController *)viewController {
	//For Google Sign-in View
	[self.navigationController pushViewController:viewController animated:YES];
}

@end
