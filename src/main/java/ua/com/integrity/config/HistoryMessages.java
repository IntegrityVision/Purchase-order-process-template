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

package ua.com.integrity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "locales/history_messages.properties", encoding = "UTF-8")
public class HistoryMessages {

    @Value("${techorder.statusDeclined}")
    private String toStatusDeclined;
    @Value("${techorder.statusApproved}")
    private String toStatusApproved;
    @Value("${techorder.adminInWarehouse}")
    private String toAdminInWarehouse;
    @Value("${techorder.adminNewOrder}")
    private String toAdminNewOrder;
    @Value("${techorder.adminDone}")
    private String toAdminDone;
    @Value("${techorder.retry}")
    private String toRetry;

    public String getToStatusDeclined() {
        return toStatusDeclined;
    }

    public String getToStatusApproved() {
        return toStatusApproved;
    }

    public String getToAdminInWarehouse() {
        return toAdminInWarehouse;
    }

    public String getToAdminNewOrder() {
        return toAdminNewOrder;
    }

    public String getToAdminDone() {
        return toAdminDone;
    }

    public String getToRetry() {
        return toRetry;
    }

}
