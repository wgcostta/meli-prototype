package com.mercadoclone.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Domain Entities Tests")
class EntityTest {

    @Nested
    @DisplayName("SellerEntity Tests")
    class SellerEntityTests {

        @Test
        @DisplayName("Should create seller with default constructor")
        void shouldCreateSellerWithDefaultConstructor() {
            SellerEntity seller = new SellerEntity();

            assertThat(seller.getId()).isNull();
            assertThat(seller.getName()).isNull();
            assertThat(seller.getLocation()).isNull();
        }

        @Test
        @DisplayName("Should create seller with parameterized constructor")
        void shouldCreateSellerWithParameterizedConstructor() {
            String id = "seller123";
            String name = "João Silva";
            String location = "São Paulo, SP";

            SellerEntity seller = new SellerEntity(id, name, location);

            assertThat(seller.getId()).isEqualTo(id);
            assertThat(seller.getName()).isEqualTo(name);
            assertThat(seller.getLocation()).isEqualTo(location);
        }

        @Test
        @DisplayName("Should set and get all properties")
        void shouldSetAndGetAllProperties() {
            SellerEntity seller = new SellerEntity();

            seller.setId("seller456");
            seller.setName("Maria Santos");
            seller.setReputation(4.8);
            seller.setLocation("Rio de Janeiro, RJ");
            seller.setIsOfficial(true);
            seller.setPositiveRating(95);
            seller.setYearsOnPlatform(5);
            seller.setAvatar("https://example.com/avatar.jpg");

            assertThat(seller.getId()).isEqualTo("seller456");
            assertThat(seller.getName()).isEqualTo("Maria Santos");
            assertThat(seller.getReputation()).isEqualTo(4.8);
            assertThat(seller.getLocation()).isEqualTo("Rio de Janeiro, RJ");
            assertThat(seller.getIsOfficial()).isTrue();
            assertThat(seller.getPositiveRating()).isEqualTo(95);
            assertThat(seller.getYearsOnPlatform()).isEqualTo(5);
            assertThat(seller.getAvatar()).isEqualTo("https://example.com/avatar.jpg");
        }

        @Nested
        @DisplayName("isTrusted() Tests")
        class IsTrustedTests {

            @Test
            @DisplayName("Should return true when seller is official")
            void shouldReturnTrueWhenSellerIsOfficial() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(true);
                seller.setReputation(3.0); // Low reputation, but is official

                assertThat(seller.isTrusted()).isTrue();
            }

            @Test
            @DisplayName("Should return true when reputation >= 4.5")
            void shouldReturnTrueWhenReputationIsHighEnough() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(false);
                seller.setReputation(4.5);

                assertThat(seller.isTrusted()).isTrue();
            }

            @Test
            @DisplayName("Should return true when reputation > 4.5")
            void shouldReturnTrueWhenReputationIsAbove45() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(false);
                seller.setReputation(4.9);

                assertThat(seller.isTrusted()).isTrue();
            }

            @Test
            @DisplayName("Should return false when not official and low reputation")
            void shouldReturnFalseWhenNotOfficialAndLowReputation() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(false);
                seller.setReputation(4.4);

                assertThat(seller.isTrusted()).isFalse();
            }

            @Test
            @DisplayName("Should return false when not official and null reputation")
            void shouldReturnFalseWhenNotOfficialAndNullReputation() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(false);
                seller.setReputation(null);

                assertThat(seller.isTrusted()).isFalse();
            }

            @Test
            @DisplayName("Should return false when official is null and low reputation")
            void shouldReturnFalseWhenOfficialIsNullAndLowReputation() {
                SellerEntity seller = new SellerEntity();
                seller.setIsOfficial(null);
                seller.setReputation(4.0);

                assertThat(seller.isTrusted()).isFalse();
            }
        }

        @Nested
        @DisplayName("equals() Tests")
        class EqualsTests {

            @Test
            @DisplayName("Should be equal to itself")
            void shouldBeEqualToItself() {
                SellerEntity seller = new SellerEntity("123", "Test", "Location");

                assertThat(seller.equals(seller)).isTrue();
            }

            @Test
            @DisplayName("Should return false when compared with null")
            void shouldReturnFalseWhenComparedWithNull() {
                SellerEntity seller = new SellerEntity("123", "Test", "Location");

                assertThat(seller.equals(null)).isFalse();
            }

            @Test
            @DisplayName("Should return false when compared with different class")
            void shouldReturnFalseWhenComparedWithDifferentClass() {
                SellerEntity seller = new SellerEntity("123", "Test", "Location");
                String other = "test";

                assertThat(seller.equals(other)).isFalse();
            }

            @Test
            @DisplayName("Should be equal when IDs are equal")
            void shouldBeEqualWhenIdsAreEqual() {
                SellerEntity seller1 = new SellerEntity("123", "Name1", "Location1");
                SellerEntity seller2 = new SellerEntity("123", "Name2", "Location2");

                assertThat(seller1.equals(seller2)).isTrue();
            }

            @Test
            @DisplayName("Should not be equal when IDs are different")
            void shouldNotBeEqualWhenIdsAreDifferent() {
                SellerEntity seller1 = new SellerEntity("123", "Name", "Location");
                SellerEntity seller2 = new SellerEntity("456", "Name", "Location");

                assertThat(seller1.equals(seller2)).isFalse();
            }
        }

        @Test
        @DisplayName("Should return same hashCode for equal objects")
        void shouldReturnSameHashCodeForEqualObjects() {
            SellerEntity seller1 = new SellerEntity("123", "Name1", "Location1");
            SellerEntity seller2 = new SellerEntity("123", "Name2", "Location2");

            assertThat(seller1.hashCode()).isEqualTo(seller2.hashCode());
        }

        @Nested
        @DisplayName("ProductImageEntity Tests")
        class ProductImageEntityTests {

            @Test
            @DisplayName("Should create image with default constructor")
            void shouldCreateImageWithDefaultConstructor() {
                ProductImageEntity image = new ProductImageEntity();

                assertThat(image.getId()).isNull();
                assertThat(image.getUrl()).isNull();
                assertThat(image.getAlt()).isNull();
                assertThat(image.getOrder()).isNull();
            }

            @Test
            @DisplayName("Should create image with parameterized constructor")
            void shouldCreateImageWithParameterizedConstructor() {
                String id = "img123";
                String url = "https://example.com/image.jpg";
                String alt = "Example product";
                Integer order = 1;

                ProductImageEntity image = new ProductImageEntity(id, url, alt, order);

                assertThat(image.getId()).isEqualTo(id);
                assertThat(image.getUrl()).isEqualTo(url);
                assertThat(image.getAlt()).isEqualTo(alt);
                assertThat(image.getOrder()).isEqualTo(order);
            }

            @Test
            @DisplayName("Should set and get all properties")
            void shouldSetAndGetAllProperties() {
                ProductImageEntity image = new ProductImageEntity();

                image.setId("img456");
                image.setUrl("https://example.com/image2.jpg");
                image.setAlt("Product image");
                image.setOrder(2);

                assertThat(image.getId()).isEqualTo("img456");
                assertThat(image.getUrl()).isEqualTo("https://example.com/image2.jpg");
                assertThat(image.getAlt()).isEqualTo("Product image");
                assertThat(image.getOrder()).isEqualTo(2);
            }

            @Nested
            @DisplayName("equals() Tests")
            class EqualsTests {

                @Test
                @DisplayName("Should be equal to itself")
                void shouldBeEqualToItself() {
                    ProductImageEntity image = new ProductImageEntity("123", "url", "alt", 1);

                    assertThat(image.equals(image)).isTrue();
                }

                @Test
                @DisplayName("Should return false when compared with null")
                void shouldReturnFalseWhenComparedWithNull() {
                    ProductImageEntity image = new ProductImageEntity("123", "url", "alt", 1);

                    assertThat(image.equals(null)).isFalse();
                }

                @Test
                @DisplayName("Should return false when compared with different class")
                void shouldReturnFalseWhenComparedWithDifferentClass() {
                    ProductImageEntity image = new ProductImageEntity("123", "url", "alt", 1);
                    String other = "test";

                    assertThat(image.equals(other)).isFalse();
                }

                @Test
                @DisplayName("Should be equal when IDs are equal")
                void shouldBeEqualWhenIdsAreEqual() {
                    ProductImageEntity image1 = new ProductImageEntity("123", "url1", "alt1", 1);
                    ProductImageEntity image2 = new ProductImageEntity("123", "url2", "alt2", 2);

                    assertThat(image1.equals(image2)).isTrue();
                }

                @Test
                @DisplayName("Should not be equal when IDs are different")
                void shouldNotBeEqualWhenIdsAreDifferent() {
                    ProductImageEntity image1 = new ProductImageEntity("123", "url", "alt", 1);
                    ProductImageEntity image2 = new ProductImageEntity("456", "url", "alt", 1);

                    assertThat(image1.equals(image2)).isFalse();
                }
            }

            @Test
            @DisplayName("Should return same hashCode for equal objects")
            void shouldReturnSameHashCodeForEqualObjects() {
                ProductImageEntity image1 = new ProductImageEntity("123", "url1", "alt1", 1);
                ProductImageEntity image2 = new ProductImageEntity("123", "url2", "alt2", 2);

                assertThat(image1.hashCode()).isEqualTo(image2.hashCode());
            }
        }

        @Nested
        @DisplayName("PaymentMethodEntity Tests")
        class PaymentMethodEntityTests {

            @Test
            @DisplayName("Should create payment method with all parameters")
            void shouldCreatePaymentMethodWithAllParameters() {
                String type = "CREDIT_CARD";
                String name = "Credit Card";
                String icon = "credit-card-icon";
                Integer installments = 12;
                Double discount = 0.05;

                PaymentMethodEntity paymentMethod = new PaymentMethodEntity(type, name, icon, installments, discount);

                assertThat(paymentMethod.type()).isEqualTo(type);
                assertThat(paymentMethod.name()).isEqualTo(name);
                assertThat(paymentMethod.icon()).isEqualTo(icon);
                assertThat(paymentMethod.installments()).isEqualTo(installments);
                assertThat(paymentMethod.discount()).isEqualTo(discount);
            }

            @Nested
            @DisplayName("allowsInstallments() Tests")
            class AllowsInstallmentsTests {

                @Test
                @DisplayName("Should return true when installments > 1")
                void shouldReturnTrueWhenInstallmentsGreaterThanOne() {
                    PaymentMethodEntity paymentMethod = new PaymentMethodEntity("CARD", "Card", "icon", 12, 0.0);

                    assertThat(paymentMethod.allowsInstallments()).isTrue();
                }

                @Test
                @DisplayName("Should return false when installments = 1")
                void shouldReturnFalseWhenInstallmentsEqualsOne() {
                    PaymentMethodEntity paymentMethod = new PaymentMethodEntity("PIX", "PIX", "pix-icon", 1, 0.1);

                    assertThat(paymentMethod.allowsInstallments()).isFalse();
                }

                @Test
                @DisplayName("Should return false when installments is null")
                void shouldReturnFalseWhenInstallmentsIsNull() {
                    PaymentMethodEntity paymentMethod = new PaymentMethodEntity("BANK_TRANSFER", "Bank Transfer", "bank-icon", null, 0.0);

                    assertThat(paymentMethod.allowsInstallments()).isFalse();
                }

                @Test
                @DisplayName("Should return false when installments = 0")
                void shouldReturnFalseWhenInstallmentsIsZero() {
                    PaymentMethodEntity paymentMethod = new PaymentMethodEntity("CASH", "Cash", "cash-icon", 0, 0.0);

                    assertThat(paymentMethod.allowsInstallments()).isFalse();
                }
            }

            @Test
            @DisplayName("Records should have automatic equals and hashCode")
            void shouldHaveAutomaticEqualsAndHashCode() {
                PaymentMethodEntity payment1 = new PaymentMethodEntity("CARD", "Card", "icon", 12, 0.05);
                PaymentMethodEntity payment2 = new PaymentMethodEntity("CARD", "Card", "icon", 12, 0.05);
                PaymentMethodEntity payment3 = new PaymentMethodEntity("PIX", "PIX", "pix", 1, 0.1);

                assertThat(payment1).isEqualTo(payment2);
                assertThat(payment1).isNotEqualTo(payment3);
                assertThat(payment1.hashCode()).isEqualTo(payment2.hashCode());
            }
        }

        @Nested
        @DisplayName("ProductRatingEntity Tests")
        class ProductRatingEntityTests {

            @Test
            @DisplayName("Should create rating with default constructor")
            void shouldCreateRatingWithDefaultConstructor() {
                ProductRatingEntity rating = new ProductRatingEntity();

                assertThat(rating.getAverage()).isNull();
                assertThat(rating.getCount()).isNull();
                assertThat(rating.getDistribution()).isNull();
            }

            @Test
            @DisplayName("Should create rating with parameterized constructor")
            void shouldCreateRatingWithParameterizedConstructor() {
                Double average = 4.5;
                Integer count = 100;
                Map<String, Integer> distribution = new HashMap<>();
                distribution.put("5", 50);
                distribution.put("4", 30);
                distribution.put("3", 15);
                distribution.put("2", 3);
                distribution.put("1", 2);

                ProductRatingEntity rating = new ProductRatingEntity(average, count, distribution);

                assertThat(rating.getAverage()).isEqualTo(average);
                assertThat(rating.getCount()).isEqualTo(count);
                assertThat(rating.getDistribution()).isEqualTo(distribution);
            }

            @Test
            @DisplayName("Should set and get all properties")
            void shouldSetAndGetAllProperties() {
                ProductRatingEntity rating = new ProductRatingEntity();
                Map<String, Integer> distribution = new HashMap<>();
                distribution.put("5", 25);
                distribution.put("4", 20);

                rating.setAverage(4.2);
                rating.setCount(45);
                rating.setDistribution(distribution);

                assertThat(rating.getAverage()).isEqualTo(4.2);
                assertThat(rating.getCount()).isEqualTo(45);
                assertThat(rating.getDistribution()).isEqualTo(distribution);
            }

            @Nested
            @DisplayName("hasGoodRating() Tests")
            class HasGoodRatingTests {

                @Test
                @DisplayName("Should return true when average >= 4.0")
                void shouldReturnTrueWhenAverageIsGreaterOrEqualToFour() {
                    ProductRatingEntity rating = new ProductRatingEntity();
                    rating.setAverage(4.0);

                    assertThat(rating.hasGoodRating()).isTrue();
                }

                @Test
                @DisplayName("Should return true when average > 4.0")
                void shouldReturnTrueWhenAverageIsGreaterThanFour() {
                    ProductRatingEntity rating = new ProductRatingEntity();
                    rating.setAverage(4.8);

                    assertThat(rating.hasGoodRating()).isTrue();
                }

                @Test
                @DisplayName("Should return false when average < 4.0")
                void shouldReturnFalseWhenAverageIsLessThanFour() {
                    ProductRatingEntity rating = new ProductRatingEntity();
                    rating.setAverage(3.9);

                    assertThat(rating.hasGoodRating()).isFalse();
                }

                @Test
                @DisplayName("Should return false when average is null")
                void shouldReturnFalseWhenAverageIsNull() {
                    ProductRatingEntity rating = new ProductRatingEntity();
                    rating.setAverage(null);

                    assertThat(rating.hasGoodRating()).isFalse();
                }
            }
        }

        @Nested
        @DisplayName("ShippingEntity Tests")
        class ShippingEntityTests {

            @Test
            @DisplayName("Should create shipping with default constructor")
            void shouldCreateShippingWithDefaultConstructor() {
                ShippingEntity shipping = new ShippingEntity();

                assertThat(shipping.getFree()).isNull();
                assertThat(shipping.getEstimatedDays()).isNull();
                assertThat(shipping.getCost()).isNull();
                assertThat(shipping.getDescription()).isNull();
            }

            @Test
            @DisplayName("Should create shipping with parameterized constructor")
            void shouldCreateShippingWithParameterizedConstructor() {
                Boolean free = true;
                Integer estimatedDays = 3;
                Double cost = 0.0;
                String description = "Free shipping";

                ShippingEntity shipping = new ShippingEntity(free, estimatedDays, cost, description);

                assertThat(shipping.getFree()).isEqualTo(free);
                assertThat(shipping.getEstimatedDays()).isEqualTo(estimatedDays);
                assertThat(shipping.getCost()).isEqualTo(cost);
                assertThat(shipping.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Should set and get all properties")
            void shouldSetAndGetAllProperties() {
                ShippingEntity shipping = new ShippingEntity();

                shipping.setFree(false);
                shipping.setEstimatedDays(5);
                shipping.setCost(15.99);
                shipping.setDescription("Standard shipping");

                assertThat(shipping.getFree()).isFalse();
                assertThat(shipping.getEstimatedDays()).isEqualTo(5);
                assertThat(shipping.getCost()).isEqualTo(15.99);
                assertThat(shipping.getDescription()).isEqualTo("Standard shipping");
            }

            @Nested
            @DisplayName("isExpress() Tests")
            class IsExpressTests {

                @Test
                @DisplayName("Should return true when estimatedDays <= 2")
                void shouldReturnTrueWhenEstimatedDaysLessOrEqualToTwo() {
                    ShippingEntity shipping = new ShippingEntity();
                    shipping.setEstimatedDays(2);

                    assertThat(shipping.isExpress()).isTrue();
                }

                @Test
                @DisplayName("Should return true when estimatedDays = 1")
                void shouldReturnTrueWhenEstimatedDaysEqualsOne() {
                    ShippingEntity shipping = new ShippingEntity();
                    shipping.setEstimatedDays(1);

                    assertThat(shipping.isExpress()).isTrue();
                }

                @Test
                @DisplayName("Should return false when estimatedDays > 2")
                void shouldReturnFalseWhenEstimatedDaysGreaterThanTwo() {
                    ShippingEntity shipping = new ShippingEntity();
                    shipping.setEstimatedDays(3);

                    assertThat(shipping.isExpress()).isFalse();
                }

                @Test
                @DisplayName("Should return false when estimatedDays is null")
                void shouldReturnFalseWhenEstimatedDaysIsNull() {
                    ShippingEntity shipping = new ShippingEntity();
                    shipping.setEstimatedDays(null);

                    assertThat(shipping.isExpress()).isFalse();
                }
            }
        }
    }

}