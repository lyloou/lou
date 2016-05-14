package com.lyloou.lou.task;

import android.os.AsyncTask;

public abstract class LouTask<Params, Progress, Result> {
	private final AsyncTask<Params, Progress, Result> mTask;

	public LouTask() {
		mTask = new AsyncTask<Params, Progress, Result>() {

			@Override
			protected void onPreExecute() {
				doBefore();
				super.onPreExecute();
			}

			@Override
			protected Result doInBackground(Params... params) {
				return doBackground(params);
			}

			@Override
			protected void onPostExecute(Result result) {
				doAfter(result);
			}

			@Override
			protected void onProgressUpdate(Progress... values) {
				doProgressUpdate(values);
			}
			
			
		};
	}

	public AsyncTask<Params, Progress, Result> getAsyncTask() {
		return mTask;
	}

	public void start(Params... params) {
		if (mTask != null)
			mTask.execute(params);
	}

	// TODO（取消，并不是真正的取消，只是标记了状态）
	public void cancel() {
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	protected void doBefore() {
	};

	protected abstract Result doBackground(Params... params);

	protected void doAfter(Result result) {
	};

	protected void doProgressUpdate(Progress... values) {
	}
}
