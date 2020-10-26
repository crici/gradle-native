package dev.nokee.platform.ios.fixtures.elements

import dev.gradleplugins.fixtures.sources.SourceElement
import dev.gradleplugins.fixtures.sources.SourceFile

class NokeeAppBaseLanguage extends SourceElement {
	private final NokeeAppMainStoryboard mainStoryboard

	NokeeAppBaseLanguage(boolean useCustomModuleProvider = false) {
		this.mainStoryboard = new NokeeAppMainStoryboard(useCustomModuleProvider)
	}

	@Override
	List<SourceFile> getFiles() {
		return [sourceFile('resources/Base.lproj', 'LaunchScreen.storyboard', '''<?xml version="1.0" encoding="UTF-8"?>
<document type="com.apple.InterfaceBuilder3.CocoaTouch.Storyboard.XIB" version="3.0" toolsVersion="15705" targetRuntime="iOS.CocoaTouch" propertyAccessControl="none" useAutolayout="YES" launchScreen="YES" useTraitCollections="YES" useSafeAreas="YES" colorMatched="YES" initialViewController="01J-lp-oVM">
    <device id="retina6_1" orientation="portrait" appearance="light"/>
    <dependencies>
        <plugIn identifier="com.apple.InterfaceBuilder.IBCocoaTouchPlugin" version="15706"/>
        <capability name="Safe area layout guides" minToolsVersion="9.0"/>
        <capability name="documents saved in the Xcode 8 format" minToolsVersion="8.0"/>
    </dependencies>
    <scenes>
        <!--View Controller-->
        <scene sceneID="EHf-IW-A2E">
            <objects>
                <viewController id="01J-lp-oVM" sceneMemberID="viewController">
                    <view key="view" contentMode="scaleToFill" id="Ze5-6b-2t3">
                        <rect key="frame" x="0.0" y="0.0" width="414" height="896"/>
                        <autoresizingMask key="autoresizingMask" widthSizable="YES" heightSizable="YES"/>
                        <subviews>
                            <imageView clipsSubviews="YES" userInteractionEnabled="NO" contentMode="scaleAspectFit" horizontalHuggingPriority="251" verticalHuggingPriority="251" fixedFrame="YES" image="full-green" translatesAutoresizingMaskIntoConstraints="NO" id="EGB-XM-Zm4">
                                <rect key="frame" x="87" y="261" width="240" height="128"/>
                                <autoresizingMask key="autoresizingMask" flexibleMaxX="YES" flexibleMaxY="YES"/>
                            </imageView>
                        </subviews>
                        <color key="backgroundColor" red="0.01663230173" green="0.077580057080000001" blue="0.12941935660000001" alpha="1" colorSpace="custom" customColorSpace="displayP3"/>
                        <viewLayoutGuide key="safeArea" id="6Tk-OE-BBY"/>
                    </view>
                </viewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="iYj-Kq-Ea1" userLabel="First Responder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="52.173913043478265" y="375"/>
        </scene>
    </scenes>
    <resources>
        <image name="full-green" width="200" height="50"/>
    </resources>
</document>
''')] + mainStoryboard.getFiles()
	}
}

class NokeeAppMainStoryboard extends SourceElement {
	private final boolean useCustomModuleProvider

	NokeeAppMainStoryboard(boolean useCustomModuleProvider) {
		this.useCustomModuleProvider = useCustomModuleProvider
	}

	@Override
	List<SourceFile> getFiles() {
		return [sourceFile('resources/Base.lproj', 'Main.storyboard', """<?xml version="1.0" encoding="UTF-8"?>
<document type="com.apple.InterfaceBuilder3.CocoaTouch.Storyboard.XIB" version="3.0" toolsVersion="15705" targetRuntime="iOS.CocoaTouch" propertyAccessControl="none" useAutolayout="YES" useTraitCollections="YES" useSafeAreas="YES" colorMatched="YES" initialViewController="BYZ-38-t0r">
    <device id="retina6_1" orientation="portrait" appearance="light"/>
    <dependencies>
        <plugIn identifier="com.apple.InterfaceBuilder.IBCocoaTouchPlugin" version="15706"/>
        <capability name="Safe area layout guides" minToolsVersion="9.0"/>
        <capability name="documents saved in the Xcode 8 format" minToolsVersion="8.0"/>
    </dependencies>
    <scenes>
        <!--View Controller-->
        <scene sceneID="tne-QT-ifu">
            <objects>
                <viewController id="BYZ-38-t0r" customClass="ViewController" ${customModuleProvider} sceneMemberID="viewController">
                    <view key="view" contentMode="scaleToFill" id="8bC-Xf-vdC">
                        <rect key="frame" x="0.0" y="0.0" width="414" height="896"/>
                        <autoresizingMask key="autoresizingMask" widthSizable="YES" heightSizable="YES"/>
                        <subviews>
                            <label opaque="NO" userInteractionEnabled="NO" contentMode="left" horizontalHuggingPriority="251" verticalHuggingPriority="251" fixedFrame="YES" text="Hello, Nokee!" textAlignment="center" lineBreakMode="tailTruncation" baselineAdjustment="alignBaselines" adjustsFontSizeToFit="NO" translatesAutoresizingMaskIntoConstraints="NO" id="VWS-Ti-KrK">
                                <rect key="frame" x="130" y="438" width="154" height="21"/>
                                <autoresizingMask key="autoresizingMask" flexibleMaxX="YES" flexibleMaxY="YES"/>
                                <fontDescription key="fontDescription" type="system" pointSize="23"/>
                                <color key="textColor" systemColor="secondarySystemBackgroundColor" red="0.94901960780000005" green="0.94901960780000005" blue="0.96862745100000003" alpha="1" colorSpace="custom" customColorSpace="sRGB"/>
                                <nil key="highlightedColor"/>
                            </label>
                            <imageView clipsSubviews="YES" userInteractionEnabled="NO" contentMode="scaleAspectFit" horizontalHuggingPriority="251" verticalHuggingPriority="251" fixedFrame="YES" image="full-green" translatesAutoresizingMaskIntoConstraints="NO" id="skV-QI-C8Y">
                                <rect key="frame" x="87" y="261" width="240" height="128"/>
                                <autoresizingMask key="autoresizingMask" flexibleMaxX="YES" flexibleMaxY="YES"/>
                            </imageView>
                        </subviews>
                        <color key="backgroundColor" red="0.01663230173" green="0.077580057080000001" blue="0.12941935660000001" alpha="1" colorSpace="custom" customColorSpace="displayP3"/>
                        <viewLayoutGuide key="safeArea" id="6Tk-OE-BBY"/>
                    </view>
                </viewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="dkx-z0-nzr" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="137.68115942028987" y="137.94642857142856"/>
        </scene>
    </scenes>
    <resources>
        <image name="full-green" width="200" height="50"/>
    </resources>
</document>
""")]
	}

	private String getCustomModuleProvider() {
		if (useCustomModuleProvider) {
			return 'customModuleProvider="target"'
		}
		return ''
	}
}
