/**
 * Copyright (C) 2013 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.textsecure.push;

import org.whispersystems.textsecure.util.Base64;

public class OutgoingPushMessage implements PushMessage {

  private int    type;
  private String destination;
  private String body;
  private String relay;

  public OutgoingPushMessage(String destination, byte[] body, int type) {
    this(null, destination, body, type);
  }

  public OutgoingPushMessage(String relay, String destination, byte[] body, int type) {
    this.relay       = relay;
    this.destination = destination;
    this.body        = Base64.encodeBytes(body);
    this.type        = type;
  }

  public String getDestination() {
    return destination;
  }

  public String getBody() {
    return body;
  }

  public int getType() {
    return type;
  }

  public String getRelay() {
    return relay;
  }
}
