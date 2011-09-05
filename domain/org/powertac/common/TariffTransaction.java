/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.powertac.common;

import org.joda.time.Instant;
import org.powertac.common.enumerations.TariffTransactionType;
import org.powertac.common.xml.BrokerConverter;
import org.powertac.common.xml.CustomerConverter;
import org.powertac.common.xml.TariffSpecificationConverter;

import com.thoughtworks.xstream.annotations.*;

/**
 * A {@code TariffTransaction} instance represents the quantity of energy consumed
 * ({@code quantity < 0}) or produced {@code quantity > 0} by some members of a 
 * specific customer model, in a specific timeslot, under a particular tariff.
 * Note that this is an immutable type, and therefore is not auditable. Instances
 * are created by the TariffMarket and communicated to brokers to represent customer
 * interactions with tariffs (subscribe, consume, etc.).
 *
 * @author Carsten Block, John Collins
 */
@XStreamAlias("tariff-tx")
public class TariffTransaction //implements Serializable
{
  @XStreamAsAttribute
  private Long id = IdGenerator.createId();

  /** Whose transaction is this? */
  @XStreamConverter(BrokerConverter.class)
  private Broker broker;

  /** Purpose of this transaction */
  @XStreamAsAttribute
  private TariffTransactionType txType = TariffTransactionType.CONSUME;

  /** The customerInfo or more precisely his meter that is being read */
  @XStreamConverter(CustomerConverter.class)
  private CustomerInfo customerInfo;

  /** Number of individual customers involved */
  @XStreamAsAttribute
  private int customerCount = 0;

  /** The timeslot for which this meter reading is generated */
  private Instant postedTime;

  /** The total quantity of energy consumed (> 0) or produced (< 0) in kWh.
   *  Note that this is not per-individual in a population model, but rather
   *  aggregate usage by customerCount individuals. */
  @XStreamAsAttribute
  private double quantity = 0.0;

  /** The total charge for this reading, according to the tariff:
   *  positive for credit to broker, negative for debit from broker */
  @XStreamAsAttribute
  private double charge = 0.0;

  @XStreamConverter(TariffSpecificationConverter.class)
  private TariffSpecification tariffSpec;
  
  public TariffTransaction (Broker broker, Instant when, 
                            TariffTransactionType txType,
                            TariffSpecification spec, 
                            CustomerInfo customer,
                            int customerCount,
                            double quantity, double charge)
  {
    super();
  }

  public String toString() {
    return("TariffTx-customer" + customerInfo.getId() + "-" +
           postedTime.getMillis()/TimeService.HOUR + "-" +
           txType + "-" + quantity + "-" + charge);
  }
}