package com.baidu.iov.dueros.test.design_patterns;

/**
 * 工厂方法模式
 * 应用场景:1.当你不知道该使用对象的确切类型的时候
 *         2.当你希望为库或框架提供扩展其内部组件方法时
 * 优点:1.将具体产品和创建者结偶 2.符合单一职责原则 3.符合开闭原则
 *
 * @author v_lining05
 * @date 2020-08-21
 */
public class FactoryMethod {

    public static void main(String[] args) {
        PublicFactory product = new CreateProductA();
        ProductA productA = (ProductA) product.createProduct();
        productA.method();
    }
}

interface Product {
    void method();
}

class ProductA implements Product {

    @Override
    public void method() {
        System.out.println("我是ProductA");
    }
}

class ProductB implements Product {

    @Override
    public void method() {
        System.out.println("我是ProductB");
    }
}

/**
 * 工厂
 */
abstract class PublicFactory {

    /**
     * 工厂方法
     * @return
     */
    abstract Product createProduct();
}

/**
 * 工厂生产出的ProductA
 */
class CreateProductA extends PublicFactory {

    @Override
    Product createProduct() {
        return new ProductA();
    }
}

/**
 * 工厂生产出的ProductB
 */
class CreateProductB extends PublicFactory {

    @Override
    Product createProduct() {
        return new ProductB();
    }
}