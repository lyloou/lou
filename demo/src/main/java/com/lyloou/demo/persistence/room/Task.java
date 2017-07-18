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

package com.lyloou.demo.persistence.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey
    public final long id;
    public String summary;
    public String description;
    public boolean done;

    public Task(long id, String summary, String description, boolean done) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.done = done;
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", summary=" + summary +
                ", description=" + description +
                ", done=" + done +
                "}";
    }

    public static class TaskBuilder {
        private long id;
        private String summary = "";
        private String description = "";
        private boolean done = false;

        public TaskBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public TaskBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder setDone(boolean done) {
            this.done = done;
            return this;
        }

        public Task build() {
            return new Task(id, summary, description, done);
        }
    }
}
