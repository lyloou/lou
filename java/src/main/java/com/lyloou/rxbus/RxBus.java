/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static volatile RxBus rxBus;
    private final Subject<Object> subject = PublishSubject.create().toSerialized();

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void post(Object o) {
        subject.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return subject.ofType(eventType);
    }
}
