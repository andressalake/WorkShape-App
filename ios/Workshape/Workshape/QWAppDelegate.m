//
//  QWAppDelegate.m
//  Workshape
//
//  Created by Kris on 2014-05-20.
//  Copyright (c) 2014 Qwantech. All rights reserved.
//

#import "QWAppDelegate.h"
#import <GooglePlus/GooglePlus.h>
#import <FacebookSDK/FacebookSDK.h>

@interface QWAppDelegate ()
@end

@implementation QWAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
	
	self.motionManager = [[CMMotionManager alloc] init];
	self.user = [[NSMutableDictionary alloc] init];
	//self.serverAddress = @"http://beta.qwantech.com:9013";
	self.serverAddress = @"http://Santoku.local:9000";
	//self.serverAddress = @"http://192.168.1.105:8085";
	
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
	[FBAppCall handleDidBecomeActive];
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark Sign In

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
	
	//if (url.scheme == "workshape") {
	log_d(@"url", @"scheme %@",url.scheme);
	log_d(@"url", @"query %@", url.query);
	//}
	
	BOOL googleLogin = [GPPURLHandler handleURL:url sourceApplication:sourceApplication annotation:annotation];	//Forward the notification to google's sign in handler
	BOOL facebookLogin = [FBAppCall handleOpenURL:url sourceApplication:sourceApplication];	//Forward the notification to Facebook's sign in handler
	
	log_d(@"login", @"annotation",annotation);
	
	//[[NSNotificationCenter defaultCenter] postNotificationName:@"login" object:annotation];
	
	return googleLogin || facebookLogin;
}

- (void)loginAndPerformSelector:(SEL)selector withObject:(id)target {
	NSString *serverAddress = [NSString stringWithFormat:@"%@/users/",self.serverAddress];
	log_d(@"login",@"server url %@",serverAddress);
	NSURL *serverURL = [NSURL URLWithString:serverAddress];
	NSMutableURLRequest *restRequest = [[NSMutableURLRequest alloc] initWithURL:serverURL];
	restRequest.HTTPMethod = @"POST";
	[restRequest setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
	[restRequest setValue:@"application/json" forHTTPHeaderField:@"Accept"];
	
	NSError *error;
	NSData *jsonSerialization = [NSJSONSerialization dataWithJSONObject:self.user options:0 error:&error];
	if (error) {
		log_e(@"login", @"Login JSON error %@",error.localizedDescription);
		[target performSelectorOnMainThread:selector withObject:error waitUntilDone:NO];
	} else {
		restRequest.HTTPBody = jsonSerialization;
		[NSURLConnection sendAsynchronousRequest:restRequest queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
			if (data) {
				NSError *error;
				NSDictionary *returnDictionary = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
				if (error) {
					[target performSelectorOnMainThread:selector withObject:error waitUntilDone:NO];
				} else {
					[self.user setObject:[returnDictionary objectForKey:@"userID"] forKey:@"userID"];
					[target performSelectorOnMainThread:selector withObject:nil waitUntilDone:NO];
				}
			} else {
				[target performSelectorOnMainThread:selector withObject:error waitUntilDone:NO];
			}
		}];
	}
}

- (void)acceptInvitation:(NSString *)inviteID {
	
}

@end
