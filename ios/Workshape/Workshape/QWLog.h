//
//  QWLog.h
//  Capture
//
//  Created by Kris on 11/28/2013.
//  Copyright (c) 2013 Qwantech. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QWLog : NSObject

void log_d(NSString *tag, NSString *format,...);	//debug
void log_i(NSString *tag, NSString *format,...);	//info
void log_w(NSString *tag, NSString *format,...);	//warning
void log_e(NSString *tag, NSString *format,...);	//error

@end
