error id: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/abstract_factory/DigitalInvoicePolicy.java:com/eoms/entity/Product#
file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/abstract_factory/DigitalInvoicePolicy.java
empty definition using pc, found symbol in pc: com/eoms/entity/Product#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 61
uri: file:///D:/Dropbox/Family/Ali%20EUI/Software%20Design%20and%20Development/Project/eoms/eoms/src/main/java/com/eoms/abstract_factory/DigitalInvoicePolicy.java
text:
```scala
package com.eoms.abstract_factory;

import com.eoms.entity.@@Product;

public class DigitalInvoicePolicy extends AbstractInvoicePolicy {

    @Override
    public double calculateExtraFees(Product product) {
        double fees = 0.0;
        logFees(fees);
        return fees;
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: com/eoms/entity/Product#