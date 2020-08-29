package com.baidu.iov.dueros.test.design_patterns;

/**
 * 抽象工厂模式
 *
 * @author v_lining05
 * @date 2020-08-21
 */
public class AbstractFactory {

    public static void main(String[] args) {
        AbstractProductFactory productFactory = new CreateFactoryC();
        AbstractProduct product = productFactory.createProduct();
        product.method();
    }
}

/**
 * 产品抽象
 */
abstract class AbstractProduct {
    abstract void method();
}

/**
 * 工厂抽象
 */
interface AbstractProductFactory {

    AbstractProduct createProduct();
}

class ProductC extends AbstractProduct {

    @Override
    public void method() {
        System.out.println("我是ProductC");
    }
}

class ProductD extends AbstractProduct {

    @Override
    public void method() {
        System.out.println("我是ProductD");
    }
}


class CreateFactoryC implements AbstractProductFactory {

    @Override
    public AbstractProduct createProduct() {
        return new ProductC();
    }
}

class CreateFactoryD implements AbstractProductFactory {

    @Override
    public AbstractProduct createProduct() {
        return new ProductD();
    }
}