import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column.js';
import '@vaadin/radio-group/theme/lumo/vaadin-radio-group.js';
import '@vaadin/radio-group/theme/lumo/vaadin-radio-button.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import 'Frontend/generated/jar-resources/dndConnector.js';
import '@vaadin/details/theme/lumo/vaadin-details.js';
import '@vaadin/dialog/theme/lumo/vaadin-dialog.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-column-group.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav-item.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/grid/theme/lumo/vaadin-grid.js';
import '@vaadin/grid/theme/lumo/vaadin-grid-sorter.js';
import '@vaadin/checkbox/theme/lumo/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.js';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/app-layout/theme/lumo/vaadin-drawer-toggle.js';
import '@vaadin/scroller/theme/lumo/vaadin-scroller.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'db3150d824231c9214a3c1d91184b7f02395caebe481a16a29c63841121179ba') {
    pending.push(import('./chunks/chunk-a8dc2de6451e2fb9ada5149099eca63bd44616fddc004af51e688c87f61700cf.js'));
  }
  if (key === 'ad3b749f8c176a86b14fa369887e22fef51afb8165384e5736c7046030b369fa') {
    pending.push(import('./chunks/chunk-0c6ec94efe36378e73adb47fc438174852fa051b44271a0a2847a0db8ff19645.js'));
  }
  if (key === 'a7f0df9f1bf084521cb2f344b5b4a2bd632b352d0745369a572a88d4873662f3') {
    pending.push(import('./chunks/chunk-442b7bb85666e65256bd58cc7f7cc0e32749668d262b0ca1e975d264e857a80d.js'));
  }
  if (key === '32e6740ceb6c3f1c7b07d32e31339c9e61b3608e4a25949d7e1225ce64b34968') {
    pending.push(import('./chunks/chunk-8d8b0bd7d7a80985493227f01f00a96b95fd9e8bca2def1c68ee81d6438b887c.js'));
  }
  if (key === '0953a3aba85bfd8d84ac16dfabaa1ff5b6a29348b23aa805384d5abc82efb666') {
    pending.push(import('./chunks/chunk-8d8b0bd7d7a80985493227f01f00a96b95fd9e8bca2def1c68ee81d6438b887c.js'));
  }
  if (key === '1f14de00b36692c96388e07da7b4d7d0aed00b531f10ad0a09b5ef8d7301712e') {
    pending.push(import('./chunks/chunk-8d8b0bd7d7a80985493227f01f00a96b95fd9e8bca2def1c68ee81d6438b887c.js'));
  }
  if (key === '2cf06ca82cdfddb866b229a647575c62452d9c0ba3e877154ca74b3a22d738d6') {
    pending.push(import('./chunks/chunk-81110d45df7602896d2ac4725b8b3a433f64232e266b2048d170ff2d85d9542e.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;