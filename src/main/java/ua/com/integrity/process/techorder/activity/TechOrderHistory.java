/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.integrity.process.techorder.activity;

import javax.annotation.Resource;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import ua.com.integrity.config.HistoryMessages;
import ua.com.integrity.model.techorder.TechOrder;
import ua.com.integrity.process.common.ProcessHelper;

@Component
public class TechOrderHistory {

    @Resource
    private HistoryMessages historyMessages;

    public void adminResult(DelegateTask task, TechOrder techOrder) {
        if (techOrder.getWarehouse()) {
            ProcessHelper.createComment(task, historyMessages.getToAdminInWarehouse());
        } else {
            ProcessHelper.createComment(task, historyMessages.getToAdminNewOrder());
        }
    }

    public void adminDoneResult(DelegateTask task) {
        ProcessHelper.createComment(task, historyMessages.getToAdminDone());
    }

    public void ownerModifyResult(DelegateTask task) {
        Boolean retryApplication = (Boolean) task.getVariable("retryApplication");
        if (retryApplication) {
            ProcessHelper.createComment(task, historyMessages.getToRetry());
        }
    }

}
