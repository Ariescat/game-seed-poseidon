package com.mmorpg.framework.groovy

/**
 * @author Ariescat* @version 2020/3/1 11:02
 */


def s = [-5, 16, 8]
s.add(6)
s.leftShift(2)
print s
print s + 1

class Person {

}

def person = new Person()
person.metaClass.sex = 'male'
person.metaClass.aaa = { person.sex = 'famale' }
println person.sex
person.aaa()

println person.sex
