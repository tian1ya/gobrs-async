package com.gobrs.async.example.task;

import com.gobrs.async.TaskSupport;
import com.gobrs.async.task.AsyncTask;

/**
 * @program: gobrs-async-starter
 * @ClassName CService
 * @description:
 * @author: sizegang
 * @create: 2022-03-20
 **/
public class CService implements AsyncTask<Object, Object> {

    @Override
    public void prepare(Object o) {


    }

    @Override
    public Object task(Object o, TaskSupport support) {
        return null;
    }

    @Override
    public boolean nessary(Object o, TaskSupport support) {
        return false;
    }


    @Override
    public void onSuccess(TaskSupport support) {

    }

    @Override
    public void onFail(TaskSupport support) {

    }
}