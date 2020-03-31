/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

(function () {
  const tryCatchWrapper = function (callback) {
    return window.Vaadin.Flow.tryCatchWrapper(callback, 'Vaadin Split Layout', 'vaadin-split-layout-flow');
  };

  // Execute callback when predicate returns true.
  // Try again later if predicate returns false.
  function when(predicate, callback, timeout = 0) {
      if (predicate()) {
          callback();
      } else {
          setTimeout(() => when(predicate, callback, 200), timeout);
      }
  }

  function _setSplitterPosition(splitlayout, position) {
    const dimension = splitlayout.orientation === 'vertical' ? 'height' : 'width';

    const getSize = (element) => element.getBoundingClientRect()[dimension];
    const container =  getSize(splitlayout) - getSize(splitlayout.$.splitter);
    const primary = getSize(splitlayout._primaryChild);
    const secondary = getSize(splitlayout._secondaryChild);
    const sum = primary + secondary;
    const newPrimary = sum * position / 100;
    const newSecondary = sum - newPrimary;

    splitlayout._setFlexBasis(splitlayout._primaryChild, newPrimary, container);
    splitlayout._setFlexBasis(splitlayout._secondaryChild, newSecondary, container);
  }

  function _setSplitterPositionWhenReady(splitlayout, position, waitForElements) {
    const predicate = () => splitlayout._primaryChild && splitlayout._secondaryChild;
    const callback = () => _setSplitterPosition(splitlayout, position);
    if(waitForElements || splitlayout.children.length > 1) {
      when(predicate, callback);
    }
  }

  window.Vaadin.Flow.splitlayoutConnector = {
    initLazy: function (splitlayout) {
      if (splitlayout.$connector) {
        return;
      }
      splitlayout.$connector = {
        setSplitterPosition: tryCatchWrapper((position, waitForElements) => _setSplitterPositionWhenReady(splitlayout, position, waitForElements))
      }
    }
  };
})();
