/*
 * Copyright  (c) 2018 Lyloou
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

package com.lyloou.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

enum ResultType implements Serializable {
    TYPECART,
    TYPEPAY;

    private ResultType() {
    }
}

public class Ali {
    public static void main(String[] args) {
        TradeResult tr = new TradeResult();
        AliPayResult ar = new AliPayResult();
        ar.payFailedOrders = new ArrayList();
        ar.payFailedOrders.add(125081572);

        ar.paySuccessOrders = new ArrayList();
        ar.paySuccessOrders.add(125081573);
        ar.paySuccessOrders.add(125081574);
        ar.paySuccessOrders.add(125081575);

        tr.resultType = ResultType.TYPECART;
        tr.payResult = ar;

        System.out.println(tr.resultType);
        System.out.println(tr.payResult.payFailedOrders);
        System.out.println(tr.payResult.paySuccessOrders);
    }
}

class AliPayResult implements Serializable {
    public List paySuccessOrders;
    public List payFailedOrders;

    public AliPayResult() {
    }
}

class TradeResult implements Serializable {
    public ResultType resultType;
    public AliPayResult payResult;

    public TradeResult() {
    }
}