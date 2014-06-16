//
//  QWLog.m
//  Capture
//
//  Created by Kris on 11/28/2013.
//  Copyright (c) 2013 Qwantech. All rights reserved.
//	See http://mobile.tutsplus.com/tutorials/iphone/customize-nslog-for-easier-debugging/
//	and http://doing-it-wrong.mikeweller.com/2012/07/youre-doing-it-wrong-1-nslogdebug-ios.html

#import "QWLog.h"
#import <asl.h>

@implementation QWLog

void log_d(NSString *tag, NSString *format,...) {
	va_list arguments;
	va_start(arguments, format);	//use variable arguments list
	
	//NSLog only adds a newline if there's not already one, so we'll do that too
	if (![format hasSuffix:@"\n"]) {
		format = [format stringByAppendingString:@"\n"];
	}
	
	NSString *body = [[NSString alloc] initWithFormat:format arguments:arguments];
	va_end(arguments);	//stop using variable arguments list
	
	NSLog(@"#D #%@ %@",tag,body);
	
	//AddStderrOnce();
	//asl_log(NULL,NULL,ASL_LEVEL_DEBUG, "#%s %s", [tag UTF8String], [body UTF8String]);
}

void log_i(NSString *tag, NSString *format,...) {
	va_list arguments;
	va_start(arguments, format);	//use variable arguments list
	
	//NSLog only adds a newline if there's not already one, so we'll do that too
	if (![format hasSuffix:@"\n"]) {
		format = [format stringByAppendingString:@"\n"];
	}
	
	NSString *body = [[NSString alloc] initWithFormat:format arguments:arguments];
	va_end(arguments);	//stop using variable arguments list
	
	NSLog(@"#I #%@ %@",tag,body);
	
	//AddStderrOnce();
	//asl_log(NULL,NULL,ASL_LEVEL_INFO, "#%s %s", [tag UTF8String], [body UTF8String]);
}

void log_w(NSString *tag, NSString *format,...) {
	va_list arguments;
	va_start(arguments, format);	//use variable arguments list
	
	//NSLog only adds a newline if there's not already one, so we'll do that too
	if (![format hasSuffix:@"\n"]) {
		format = [format stringByAppendingString:@"\n"];
	}
	
	NSString *body = [[NSString alloc] initWithFormat:format arguments:arguments];
	va_end(arguments);	//stop using variable arguments list
	
	NSLog(@"#W #%@ %@",tag,body);
	
	//AddStderrOnce();
	//asl_log(NULL,NULL,ASL_LEVEL_WARNING, "#%s %s", [tag UTF8String], [body UTF8String]);
}

void log_e(NSString *tag, NSString *format,...) {
	va_list arguments;
	va_start(arguments, format);	//use variable arguments list
	
	//NSLog only adds a newline if there's not already one, so we'll do that too
	if (![format hasSuffix:@"\n"]) {
		format = [format stringByAppendingString:@"\n"];
	}
	
	NSString *body = [[NSString alloc] initWithFormat:format arguments:arguments];
	va_end(arguments);	//stop using variable arguments list
	
	NSLog(@"#E #%@ %@",tag,body);
	
	//AddStderrOnce();
	//asl_log(NULL,NULL,ASL_LEVEL_ERR, "#%s %s", [tag UTF8String], [body UTF8String]);
}
/*
static void AddStderrOnce() {
	//Add StdErr to logging so we can see debug and info messages in Xcode's console.  This may make things noisy.
	static dispatch_once_t onceToken;
	dispatch_once(&onceToken, ^{
		asl_add_log_file(NULL, STDERR_FILENO);
	});
}
*/

@end
